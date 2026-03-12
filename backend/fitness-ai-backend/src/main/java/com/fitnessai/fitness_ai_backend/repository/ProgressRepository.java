package com.fitnessai.fitness_ai_backend.repository;

import com.fitnessai.fitness_ai_backend.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    List<Progress> findByUserIdOrderByRecordedDateDesc(Long userId);

    Optional<Progress> findTopByUserIdOrderByRecordedDateDesc(Long userId);
}