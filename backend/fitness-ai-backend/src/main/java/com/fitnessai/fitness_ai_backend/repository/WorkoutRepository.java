package com.fitnessai.fitness_ai_backend.repository;

import com.fitnessai.fitness_ai_backend.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByUserIdOrderByDateDesc(Long userId);

    List<Workout> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT SUM(w.caloriesBurned) FROM Workout w WHERE w.user.id = :userId AND w.date = :date")
    Integer sumCaloriesBurnedByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user.id = :userId AND w.date >= :startDate")
    Long countWorkoutsSince(Long userId, LocalDate startDate);
}
