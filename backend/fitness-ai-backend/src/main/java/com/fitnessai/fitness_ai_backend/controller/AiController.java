package com.fitnessai.fitness_ai_backend.controller;

import com.fitnessai.fitness_ai_backend.ai.AiFitnessRecommendationService;
import com.fitnessai.fitness_ai_backend.ai.DietPlanResponse;
import com.fitnessai.fitness_ai_backend.ai.WorkoutPlanResponse;
import com.fitnessai.fitness_ai_backend.dto.response.ApiResponse;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Recommendations", description = "AI-generated workout and diet plans")
@SecurityRequirement(name = "bearerAuth")
public class AiController {

    private final AiFitnessRecommendationService aiService;
    private final UserRepository userRepository;

    @GetMapping("/workout-plan/{userId}")
    @Operation(summary = "Generate personalized AI workout plan for a user")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> getWorkoutPlan(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(aiService.generateWorkoutPlan(userId), "AI workout plan generated"));
    }

    @GetMapping("/diet-plan/{userId}")
    @Operation(summary = "Generate personalized AI diet/nutrition plan for a user")
    public ResponseEntity<ApiResponse<DietPlanResponse>> getDietPlan(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(aiService.generateDietPlan(userId), "AI diet plan generated"));
    }

    @GetMapping("/my-workout-plan")
    @Operation(summary = "Generate workout plan for authenticated user")
    public ResponseEntity<ApiResponse<WorkoutPlanResponse>> getMyWorkoutPlan(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(aiService.generateWorkoutPlan(userId), "AI workout plan generated"));
    }

    @GetMapping("/my-diet-plan")
    @Operation(summary = "Generate diet plan for authenticated user")
    public ResponseEntity<ApiResponse<DietPlanResponse>> getMyDietPlan(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(aiService.generateDietPlan(userId), "AI diet plan generated"));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}

