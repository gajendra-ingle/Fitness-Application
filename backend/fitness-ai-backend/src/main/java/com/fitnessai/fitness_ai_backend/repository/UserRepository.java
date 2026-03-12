package com.fitnessai.fitness_ai_backend.repository;

import com.fitnessai.fitness_ai_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

