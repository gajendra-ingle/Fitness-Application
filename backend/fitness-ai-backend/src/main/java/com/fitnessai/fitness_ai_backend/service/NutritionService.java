package com.fitnessai.fitness_ai_backend.service;

import com.fitnessai.fitness_ai_backend.dto.request.NutritionLogRequest;
import com.fitnessai.fitness_ai_backend.dto.response.NutritionResponse;

import java.time.LocalDate;
import java.util.List;

public interface NutritionService {

    NutritionResponse logNutrition(Long userId, NutritionLogRequest request);

    List<NutritionResponse> getNutritionHistory(Long userId);

    List<NutritionResponse> getNutritionByDate(Long userId, LocalDate date);

    void deleteLog(Long logId);
}