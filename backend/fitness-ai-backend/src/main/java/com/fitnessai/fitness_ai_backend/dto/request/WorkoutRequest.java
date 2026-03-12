package com.fitnessai.fitness_ai_backend.dto.request;

import com.fitnessai.fitness_ai_backend.entity.Workout;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutRequest {
    @NotBlank
    private String workoutName;

    private Workout.WorkoutType workoutType;
    private Integer duration;
    private Integer caloriesBurned;

    @NotNull
    private LocalDate date;

    private List<WorkoutExerciseRequest> exercises;

    @Data
    public static class WorkoutExerciseRequest {
        private Long exerciseId;
        private Integer sets;
        private Integer reps;
        private Double weight;
    }
}

