package com.fitnessai.fitness_ai_backend.ai;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessai.fitness_ai_backend.entity.UserProfile;
import com.fitnessai.fitness_ai_backend.exception.ResourceNotFoundException;
import com.fitnessai.fitness_ai_backend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiFitnessRecommendationService {

    private final UserProfileRepository profileRepository;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;


    public WorkoutPlanResponse generateWorkoutPlan(Long userId) {
        UserProfile profile = fetchProfile(userId);
        String prompt = buildWorkoutPrompt(profile);
        String json   = callOpenAi(prompt);
        return deserialise(json, WorkoutPlanResponse.class);
    }

    public DietPlanResponse generateDietPlan(Long userId) {
        UserProfile profile = fetchProfile(userId);
        NutritionContext ctx = buildNutritionContext(profile);
        String prompt = buildDietPrompt(profile, ctx);
        String json   = callOpenAi(prompt);

        DietPlanResponse plan = deserialise(json, DietPlanResponse.class);
        plan.setBmr(ctx.bmr);
        plan.setTdee(ctx.tdee);
        plan.setDailyCalories(ctx.targetCalories);
        plan.setProteinGrams(ctx.proteinG);
        plan.setCarbsGrams(ctx.carbsG);
        plan.setFatsGrams(ctx.fatsG);
        plan.setWaterIntakeLiters(ctx.waterL);
        return plan;
    }

    // Prompt builders
    private String buildWorkoutPrompt(UserProfile profile) {
        String goal  = safeGoal(profile);
        String level = safeLevel(profile);
        int    age   = profile.getAge()    != null ? profile.getAge()    : 30;
        double wt    = profile.getWeight() != null ? profile.getWeight() : 70;
        double ht    = profile.getHeight() != null ? profile.getHeight() : 170;
        String sex   = profile.getGender() != null ? profile.getGender().name() : "UNSPECIFIED";

        return """
                You are an expert personal trainer and sports scientist.
                Generate a personalised 7-day weekly workout plan for the following user.

                User profile:
                  - Age: %d years
                  - Sex: %s
                  - Weight: %.1f kg
                  - Height: %.1f cm
                  - Fitness goal: %s
                  - Activity level: %s

                Rules:
                  - Include rest days appropriate to the goal.
                  - Each workout day must have a workout name, duration in minutes, and a list
                    of exercises with sets, repsOrTime (integer), and unit (reps / sec / min / m).
                  - Make the plan progressive, safe, and realistic for the activity level.
                  - Include 4 practical tips tailored to this goal.

                IMPORTANT: Respond with ONLY a single raw JSON object — no markdown fences,
                no commentary, no keys outside the schema below.

                Required JSON schema:
                {
                  "goal": "<GOAL_NAME>",
                  "activityLevel": "<ACTIVITY_LEVEL>",
                  "summary": "<2-sentence plan overview>",
                  "weeklyWorkouts": <int>,
                  "restDays": <int>,
                  "weeklyPlan": [
                    {
                      "day": "<Monday|Tuesday|...>",
                      "workoutName": "<name>",
                      "durationMinutes": <int>,
                      "exercises": [
                        { "name": "<exercise>", "sets": <int>, "repsOrTime": <int>, "unit": "<reps|sec|min|m>" }
                      ]
                    }
                  ],
                  "tips": ["<tip1>", "<tip2>", "<tip3>", "<tip4>"]
                }
                """.formatted(age, sex, wt, ht, goal, level);
    }

    private String buildDietPrompt(UserProfile profile, NutritionContext ctx) {
        String goal = safeGoal(profile);
        String sex  = profile.getGender() != null ? profile.getGender().name() : "UNSPECIFIED";

        return """
                You are a registered dietitian and sports nutritionist.
                Create a personalised daily nutrition and meal plan.

                User profile:
                  - Sex: %s
                  - Fitness goal: %s
                  - Calculated BMR: %d kcal/day
                  - Calculated TDEE: %d kcal/day
                  - Target daily calories: %d kcal
                  - Protein target: %d g/day
                  - Carbohydrate target: %d g/day
                  - Fat target: %d g/day
                  - Water intake target: %.1f litres/day

                Rules:
                  - Meal names must match common meal times (Breakfast, Mid-Morning, Lunch,
                    Pre-Workout, Post-Workout, Dinner, or Evening Snack as appropriate).
                  - Each meal's calorie allocation must add up to approximately the daily target.
                  - Foods listed should be realistic, affordable whole foods.
                  - Include a list of 8-12 foods to eat more of.
                  - Include a list of 5-8 foods/habits to avoid.
                  - Include 4 practical nutrition tips for this goal.

                IMPORTANT: Respond with ONLY a single raw JSON object — no markdown fences,
                no commentary, no extra keys.

                Required JSON schema:
                {
                  "goal": "<GOAL_NAME>",
                  "dailyCalories": %d,
                  "bmr": %d,
                  "tdee": %d,
                  "proteinGrams": %d,
                  "carbsGrams": %d,
                  "fatsGrams": %d,
                  "waterIntakeLiters": %.2f,
                  "mealPlan": [
                    { "mealName": "<name>", "calories": <int>, "foods": "<comma-separated foods>" }
                  ],
                  "foodsToEat":   ["<food1>", "..."],
                  "foodsToAvoid": ["<item1>", "..."],
                  "tips":         ["<tip1>", "..."]
                }
                """.formatted(
                sex, goal,
                ctx.bmr, ctx.tdee, ctx.targetCalories,
                ctx.proteinG, ctx.carbsG, ctx.fatsG, ctx.waterL,
                // repeated for the JSON template section
                ctx.targetCalories, ctx.bmr, ctx.tdee,
                ctx.proteinG, ctx.carbsG, ctx.fatsG, ctx.waterL
        );
    }

    // OpenAI call via Spring AI ChatClient

    /**
     * Calls the configured OpenAI model and returns the raw text response.
     * Spring AI's ChatClient handles retries, token counting, and
     * the HTTP transport layer for us.
     */
    private String callOpenAi(String userPrompt) {
        log.debug("Calling OpenAI — prompt length: {} chars", userPrompt.length());

        String response = chatClient
                .prompt()
                .system("""
                        You are a professional fitness and nutrition AI assistant.
                        You ALWAYS respond with valid JSON only — no prose, no markdown fences.
                        """)
                .user(userPrompt)
                .call()
                .content();

        log.debug("OpenAI raw response: {}", response);
        return response;
    }



    private <T> T deserialise(String json, Class<T> type) {
        String cleaned = json
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("(?s)```\\s*", "")
                .trim();
        try {
            return objectMapper.readValue(cleaned, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse OpenAI response as {}: {}", type.getSimpleName(), cleaned, e);
            throw new RuntimeException(
                    "AI response could not be parsed. Please try again.", e);
        }
    }

    // Nutrition maths
    private record NutritionContext(
            int bmr, int tdee, int targetCalories,
            int proteinG, int carbsG, int fatsG, double waterL
    ) {}

    private NutritionContext buildNutritionContext(UserProfile profile) {
        double weight = profile.getWeight() != null ? profile.getWeight() : 70;
        double height = profile.getHeight() != null ? profile.getHeight() : 170;
        int    age    = profile.getAge()    != null ? profile.getAge()    : 30;
        boolean male  = profile.getGender() == UserProfile.Gender.MALE;

        UserProfile.ActivityLevel level = profile.getActivityLevel() != null
                ? profile.getActivityLevel()
                : UserProfile.ActivityLevel.MODERATELY_ACTIVE;

        UserProfile.FitnessGoal goal = profile.getFitnessGoal() != null
                ? profile.getFitnessGoal()
                : UserProfile.FitnessGoal.MAINTENANCE;

        // Mifflin-St Jeor BMR
        double bmr = male
                ? 10 * weight + 6.25 * height - 5 * age + 5
                : 10 * weight + 6.25 * height - 5 * age - 161;

        double multiplier = switch (level) {
            case SEDENTARY        -> 1.2;
            case LIGHTLY_ACTIVE   -> 1.375;
            case MODERATELY_ACTIVE-> 1.55;
            case VERY_ACTIVE      -> 1.725;
            case EXTRA_ACTIVE     -> 1.9;
        };

        double tdee   = bmr * multiplier;
        double target = switch (goal) {
            case WEIGHT_LOSS -> tdee - 500;
            case MUSCLE_GAIN -> tdee + 300;
            default          -> tdee;
        };

        double proteinG = switch (goal) {
            case MUSCLE_GAIN -> weight * 2.2;
            case WEIGHT_LOSS -> weight * 2.0;
            default          -> weight * 1.6;
        };
        double fatPct  = (goal == UserProfile.FitnessGoal.MUSCLE_GAIN) ? 0.25 : 0.30;
        double fatsG   = target * fatPct / 9;
        double carbsG  = (target - proteinG * 4 - fatsG * 9) / 4;

        return new NutritionContext(
                (int) Math.round(bmr),
                (int) Math.round(tdee),
                (int) Math.round(target),
                (int) Math.round(proteinG),
                (int) Math.round(carbsG),
                (int) Math.round(fatsG),
                Math.round(weight * 0.033 * 10.0) / 10.0
        );
    }


    // Helpers
    private UserProfile fetchProfile(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile not found. Please complete your profile first."));
    }

    private String safeGoal(UserProfile p) {
        return p.getFitnessGoal() != null
                ? p.getFitnessGoal().name()
                : UserProfile.FitnessGoal.MAINTENANCE.name();
    }

    private String safeLevel(UserProfile p) {
        return p.getActivityLevel() != null
                ? p.getActivityLevel().name()
                : UserProfile.ActivityLevel.MODERATELY_ACTIVE.name();
    }
}
