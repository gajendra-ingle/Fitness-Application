package com.fitnessai.fitness_ai_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "nutrition_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    private Integer calories;
    private Double protein;  // in grams
    private Double carbs;    // in grams
    private Double fats;     // in grams

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    private LocalDate date;

    public enum MealType {BREAKFAST, LUNCH, DINNER, SNACK}
}

