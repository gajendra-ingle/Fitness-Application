package com.fitnessai.fitness_ai_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double weight;
    private Double bmi;

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "recorded_date", nullable = false)
    private LocalDate recordedDate;

    private String notes;
}

