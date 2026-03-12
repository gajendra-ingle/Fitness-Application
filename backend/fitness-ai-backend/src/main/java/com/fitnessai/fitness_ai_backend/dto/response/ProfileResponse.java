package com.fitnessai.fitness_ai_backend.dto.response;

import com.fitnessai.fitness_ai_backend.entity.UserProfile;
import lombok.Data;

@Data
public class ProfileResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Integer age;
    private Double height;
    private Double weight;
    private UserProfile.Gender gender;
    private UserProfile.FitnessGoal fitnessGoal;
    private UserProfile.ActivityLevel activityLevel;
}
