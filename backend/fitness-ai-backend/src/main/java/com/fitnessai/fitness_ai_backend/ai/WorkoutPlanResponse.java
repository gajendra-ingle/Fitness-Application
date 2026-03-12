package com.fitnessai.fitness_ai_backend.ai;


import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanResponse {
    private String goal;
    private String activityLevel;
    private String summary;
    private int weeklyWorkouts;
    private int restDays;
    private List<DayPlan> weeklyPlan;
    private List<String> tips;

    @Data
    public static class DayPlan {
        private String day;
        private String workoutName;
        private int durationMinutes;
        private List<ExerciseItem> exercises;
    }

    @Data
    public static class ExerciseItem {
        private String name;
        private int sets;
        private int repsOrTime;
        private String unit;
    }
}

