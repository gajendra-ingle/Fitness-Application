package com.fitnessai.fitness_ai_backend.service;


import com.fitnessai.fitness_ai_backend.dto.request.LoginRequest;
import com.fitnessai.fitness_ai_backend.dto.request.RegisterRequest;
import com.fitnessai.fitness_ai_backend.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}

