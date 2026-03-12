package com.fitnessai.fitness_ai_backend.service;

import com.fitnessai.fitness_ai_backend.dto.request.WorkoutRequest;
import com.fitnessai.fitness_ai_backend.dto.response.WorkoutResponse;

import java.util.List;

public interface WorkoutService {
    
    WorkoutResponse createWorkout(Long userId, WorkoutRequest request);

    List<WorkoutResponse> getUserWorkouts(Long userId);

    WorkoutResponse getWorkoutById(Long workoutId);

    WorkoutResponse updateWorkout(Long workoutId, WorkoutRequest request);

    void deleteWorkout(Long workoutId);
}
