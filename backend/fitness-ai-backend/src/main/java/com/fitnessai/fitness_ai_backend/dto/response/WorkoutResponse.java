package com.fitnessai.fitness_ai_backend.dto.response;

import com.fitnessai.fitness_ai_backend.entity.Workout;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutResponse {
    private Long id;
    private String workoutName;
    private Workout.WorkoutType workoutType;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDate date;
    private List<ExerciseResponse> exercises;

    @Data
    public static class ExerciseResponse {
        private Long id;
        private String exerciseName;
        private String muscleGroup;
        private Integer sets;
        private Integer reps;
        private Double weight;
    }
}
