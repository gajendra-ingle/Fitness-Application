package com.fitnessai.fitness_ai_backend.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "workout_name", nullable = false)
    private String workoutName;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type")
    private WorkoutType workoutType;

    private Integer duration;  // in minutes

    @Column(name = "calories_burned")
    private Integer caloriesBurned;

    private LocalDate date;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutExercise> workoutExercises;

    public enum WorkoutType {
        CARDIO, STRENGTH, FLEXIBILITY, HIIT, YOGA, PILATES, SPORTS, OTHER
    }
}

