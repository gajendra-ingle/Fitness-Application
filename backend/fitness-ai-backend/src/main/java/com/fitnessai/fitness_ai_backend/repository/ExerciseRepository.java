package com.fitnessai.fitness_ai_backend.repository;

import com.fitnessai.fitness_ai_backend.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByMuscleGroupIgnoreCase(String muscleGroup);
    List<Exercise> findByDifficulty(Exercise.Difficulty difficulty);
}