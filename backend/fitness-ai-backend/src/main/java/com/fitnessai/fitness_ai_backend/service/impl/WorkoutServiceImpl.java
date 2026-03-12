package com.fitnessai.fitness_ai_backend.service.impl;

import com.fitnessai.fitness_ai_backend.dto.request.WorkoutRequest;
import com.fitnessai.fitness_ai_backend.dto.response.WorkoutResponse;
import com.fitnessai.fitness_ai_backend.entity.Exercise;
import com.fitnessai.fitness_ai_backend.entity.User;
import com.fitnessai.fitness_ai_backend.entity.Workout;
import com.fitnessai.fitness_ai_backend.entity.WorkoutExercise;
import com.fitnessai.fitness_ai_backend.exception.ResourceNotFoundException;
import com.fitnessai.fitness_ai_backend.repository.ExerciseRepository;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.repository.WorkoutRepository;
import com.fitnessai.fitness_ai_backend.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public WorkoutResponse createWorkout(Long userId, WorkoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Workout workout = Workout.builder()
                .user(user)
                .workoutName(request.getWorkoutName())
                .workoutType(request.getWorkoutType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .date(request.getDate())
                .build();

        if (request.getExercises() != null) {
            List<WorkoutExercise> workoutExercises = request.getExercises().stream()
                    .map(ex -> {
                        Exercise exercise = exerciseRepository.findById(ex.getExerciseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found: " + ex.getExerciseId()));
                        return WorkoutExercise.builder()
                                .workout(workout)
                                .exercise(exercise)
                                .sets(ex.getSets())
                                .reps(ex.getReps())
                                .weight(ex.getWeight())
                                .build();
                    }).collect(Collectors.toList());
            workout.setWorkoutExercises(workoutExercises);
        }

        Workout saved = workoutRepository.save(workout);
        return mapToResponse(saved);
    }

    @Override
    public List<WorkoutResponse> getUserWorkouts(Long userId) {
        return workoutRepository.findByUserIdOrderByDateDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutResponse getWorkoutById(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found: " + workoutId));
        return mapToResponse(workout);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(Long workoutId, WorkoutRequest request) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found: " + workoutId));
        workout.setWorkoutName(request.getWorkoutName());
        workout.setWorkoutType(request.getWorkoutType());
        workout.setDuration(request.getDuration());
        workout.setCaloriesBurned(request.getCaloriesBurned());
        workout.setDate(request.getDate());
        return mapToResponse(workoutRepository.save(workout));
    }

    @Override
    public void deleteWorkout(Long workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    private WorkoutResponse mapToResponse(Workout workout) {
        WorkoutResponse res = new WorkoutResponse();
        res.setId(workout.getId());
        res.setWorkoutName(workout.getWorkoutName());
        res.setWorkoutType(workout.getWorkoutType());
        res.setDuration(workout.getDuration());
        res.setCaloriesBurned(workout.getCaloriesBurned());
        res.setDate(workout.getDate());

        if (workout.getWorkoutExercises() != null) {
            res.setExercises(workout.getWorkoutExercises().stream().map(we -> {
                WorkoutResponse.ExerciseResponse er = new WorkoutResponse.ExerciseResponse();
                er.setId(we.getId());
                er.setExerciseName(we.getExercise().getName());
                er.setMuscleGroup(we.getExercise().getMuscleGroup());
                er.setSets(we.getSets());
                er.setReps(we.getReps());
                er.setWeight(we.getWeight());
                return er;
            }).collect(Collectors.toList()));
        }
        return res;
    }
}

