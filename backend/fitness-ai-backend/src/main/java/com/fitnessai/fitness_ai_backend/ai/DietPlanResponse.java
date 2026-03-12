package com.fitnessai.fitness_ai_backend.ai;


import lombok.Data;

import java.util.List;

@Data
public class DietPlanResponse {
    private String goal;
    private int dailyCalories;
    private int bmr;
    private int tdee; // Total Daily Energy Expenditure
    private int proteinGrams;
    private int carbsGrams;
    private int fatsGrams;
    private double waterIntakeLiters;
    private List<Meal> mealPlan;
    private List<String> foodsToEat;
    private List<String> foodsToAvoid;
    private List<String> tips;

    @Data
    public static class Meal {
        private String mealName;
        private int calories;
        private String foods;
    }
}

