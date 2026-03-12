package com.fitnessai.fitness_ai_backend.controller;

import com.fitnessai.fitness_ai_backend.dto.request.ProgressRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ApiResponse;
import com.fitnessai.fitness_ai_backend.dto.response.ProgressResponse;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.ProgressService;
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
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "Track fitness progress over time")
@SecurityRequirement(name = "bearerAuth")
public class ProgressController {

    private final ProgressService progressService;
    private final UserRepository userRepository;

    @PostMapping("/log")
    public ResponseEntity<ApiResponse<ProgressResponse>> logProgress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProgressRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(ApiResponse.success(progressService.logProgress(userId, request), "Progress logged"));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<ProgressResponse>>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(progressService.getProgressHistory(getUserId(userDetails)), "History retrieved"));
    }

    private Long getUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}

