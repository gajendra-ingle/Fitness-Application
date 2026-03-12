package com.fitnessai.fitness_ai_backend.service;


import com.fitnessai.fitness_ai_backend.dto.request.ProfileRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfile(Long userId);

    ProfileResponse updateProfile(Long userId, ProfileRequest request);
}

