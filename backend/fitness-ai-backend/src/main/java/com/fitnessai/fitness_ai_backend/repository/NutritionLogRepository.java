package com.fitnessai.fitness_ai_backend.repository;

import com.fitnessai.fitness_ai_backend.entity.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
    List<NutritionLog> findByUserIdOrderByDateDesc(Long userId);

    List<NutritionLog> findByUserIdAndDate(Long userId, LocalDate date);

    List<NutritionLog> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT SUM(n.calories) FROM NutritionLog n WHERE n.user.id = :userId AND n.date = :date")
    Integer sumCaloriesByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT SUM(n.protein) FROM NutritionLog n WHERE n.user.id = :userId AND n.date = :date")
    Double sumProteinByUserIdAndDate(Long userId, LocalDate date);
}
