package com.fitnessai.fitness_ai_backend.controller;

import com.fitnessai.fitness_ai_backend.dto.request.WorkoutRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ApiResponse;
import com.fitnessai.fitness_ai_backend.dto.response.WorkoutResponse;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "Workouts", description = "Workout logging and retrieval")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Log a new workout")
    public ResponseEntity<ApiResponse<WorkoutResponse>> createWorkout(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WorkoutRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(workoutService.createWorkout(userId, request), "Workout logged"));
    }

    @GetMapping
    @Operation(summary = "Get all workouts for current user")
    public ResponseEntity<ApiResponse<List<WorkoutResponse>>> getMyWorkouts(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(workoutService.getUserWorkouts(userId), "Workouts retrieved"));
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get workouts by user ID")
    public ResponseEntity<ApiResponse<List<WorkoutResponse>>> getWorkoutsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getUserWorkouts(id), "Workouts retrieved"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutResponse>> getWorkout(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.getWorkoutById(id), "Workout retrieved"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutResponse>> updateWorkout(@PathVariable Long id,
                                                                      @RequestBody WorkoutRequest request) {
        return ResponseEntity.ok(ApiResponse.success(workoutService.updateWorkout(id, request), "Workout updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Workout deleted"));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}

