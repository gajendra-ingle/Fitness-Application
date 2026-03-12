package com.fitnessai.fitness_ai_backend.dto.request;

import com.fitnessai.fitness_ai_backend.entity.UserProfile;
import lombok.Data;

@Data
public class ProfileRequest {
    private Integer age;
    private Double height;
    private Double weight;
    private UserProfile.Gender gender;
    private UserProfile.FitnessGoal fitnessGoal;
    private UserProfile.ActivityLevel activityLevel;
}
