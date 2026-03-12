package com.fitnessai.fitness_ai_backend.dto.response;

import com.fitnessai.fitness_ai_backend.entity.NutritionLog;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NutritionResponse {
    private Long id;
    private String foodName;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private NutritionLog.MealType mealType;
    private LocalDate date;
}

