package com.fitnessai.fitness_ai_backend.service.impl;

import com.fitnessai.fitness_ai_backend.dto.request.NutritionLogRequest;
import com.fitnessai.fitness_ai_backend.dto.response.NutritionResponse;
import com.fitnessai.fitness_ai_backend.entity.NutritionLog;
import com.fitnessai.fitness_ai_backend.entity.User;
import com.fitnessai.fitness_ai_backend.exception.ResourceNotFoundException;
import com.fitnessai.fitness_ai_backend.repository.NutritionLogRepository;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionServiceImpl implements NutritionService {

    private final NutritionLogRepository nutritionLogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NutritionResponse logNutrition(Long userId, NutritionLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        NutritionLog log = NutritionLog.builder()
                .user(user)
                .foodName(request.getFoodName())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fats(request.getFats())
                .mealType(request.getMealType())
                .date(request.getDate())
                .build();

        return mapToResponse(nutritionLogRepository.save(log));
    }

    @Override
    public List<NutritionResponse> getNutritionHistory(Long userId) {
        return nutritionLogRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<NutritionResponse> getNutritionByDate(Long userId, LocalDate date) {
        return nutritionLogRepository.findByUserIdAndDate(userId, date).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteLog(Long logId) {
        nutritionLogRepository.deleteById(logId);
    }

    private NutritionResponse mapToResponse(NutritionLog log) {
        NutritionResponse res = new NutritionResponse();
        res.setId(log.getId());
        res.setFoodName(log.getFoodName());
        res.setCalories(log.getCalories());
        res.setProtein(log.getProtein());
        res.setCarbs(log.getCarbs());
        res.setFats(log.getFats());
        res.setMealType(log.getMealType());
        res.setDate(log.getDate());
        return res;
    }
}
