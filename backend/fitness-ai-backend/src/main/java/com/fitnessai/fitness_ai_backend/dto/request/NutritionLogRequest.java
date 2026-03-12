package com.fitnessai.fitness_ai_backend.dto.request;

import com.fitnessai.fitness_ai_backend.entity.NutritionLog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NutritionLogRequest {

    @NotBlank
    private String foodName;

    @NotNull
    private Integer calories;

    private Double protein;
    private Double carbs;
    private Double fats;
    private NutritionLog.MealType mealType;

    @NotNull
    private LocalDate date;
}
