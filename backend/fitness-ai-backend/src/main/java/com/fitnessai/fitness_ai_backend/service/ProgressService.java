package com.fitnessai.fitness_ai_backend.service;

import com.fitnessai.fitness_ai_backend.dto.request.ProgressRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ProgressResponse;

import java.util.List;

public interface ProgressService {
    ProgressResponse logProgress(Long userId, ProgressRequest request);

    List<ProgressResponse> getProgressHistory(Long userId);
}