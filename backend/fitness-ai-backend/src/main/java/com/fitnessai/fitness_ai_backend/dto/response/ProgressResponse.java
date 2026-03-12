package com.fitnessai.fitness_ai_backend.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgressResponse {
    private Long id;
    private Double weight;
    private Double bmi;
    private Double bodyFatPercentage;
    private LocalDate recordedDate;
    private String notes;
}
