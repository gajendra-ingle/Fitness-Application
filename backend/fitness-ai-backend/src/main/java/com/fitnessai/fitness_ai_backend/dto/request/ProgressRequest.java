package com.fitnessai.fitness_ai_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgressRequest {

    @NotNull
    private Double weight;

    private Double bodyFatPercentage;

    @NotNull
    private LocalDate recordedDate;

    private String notes;
}
