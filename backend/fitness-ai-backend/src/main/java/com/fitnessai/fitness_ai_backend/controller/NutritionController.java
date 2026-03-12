package com.fitnessai.fitness_ai_backend.controller;

import com.fitnessai.fitness_ai_backend.dto.request.NutritionLogRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ApiResponse;
import com.fitnessai.fitness_ai_backend.dto.response.NutritionResponse;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
@RequiredArgsConstructor
@Tag(name = "Nutrition", description = "Nutrition logging and history")
@SecurityRequirement(name = "bearerAuth")
public class NutritionController {

    private final NutritionService nutritionService;
    private final UserRepository userRepository;

    @PostMapping("/log")
    @Operation(summary = "Log a food entry")
    public ResponseEntity<ApiResponse<NutritionResponse>> logNutrition(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NutritionLogRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(nutritionService.logNutrition(userId, request), "Nutrition logged"));
    }

    @GetMapping("/history")
    @Operation(summary = "Get full nutrition history")
    public ResponseEntity<ApiResponse<List<NutritionResponse>>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(nutritionService.getNutritionHistory(getUserId(userDetails)), "History retrieved"));
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's nutrition")
    public ResponseEntity<ApiResponse<List<NutritionResponse>>> getToday(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                nutritionService.getNutritionByDate(getUserId(userDetails), LocalDate.now()), "Today's nutrition retrieved"));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<NutritionResponse>>> getByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(
                nutritionService.getNutritionByDate(getUserId(userDetails), date), "Nutrition retrieved"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable Long id) {
        nutritionService.deleteLog(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Log deleted"));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}

