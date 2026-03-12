package com.fitnessai.fitness_ai_backend.service.impl;

import com.fitnessai.fitness_ai_backend.dto.request.ProgressRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ProgressResponse;
import com.fitnessai.fitness_ai_backend.entity.Progress;
import com.fitnessai.fitness_ai_backend.entity.User;
import com.fitnessai.fitness_ai_backend.entity.UserProfile;
import com.fitnessai.fitness_ai_backend.exception.ResourceNotFoundException;
import com.fitnessai.fitness_ai_backend.repository.ProgressRepository;
import com.fitnessai.fitness_ai_backend.repository.UserProfileRepository;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;

    @Override
    @Transactional
    public ProgressResponse logProgress(Long userId, ProgressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Calculate BMI using profile height
        double bmi = 0;
        UserProfile profile = profileRepository.findByUserId(userId).orElse(null);
        if (profile != null && profile.getHeight() != null && profile.getHeight() > 0) {
            double heightM = profile.getHeight() / 100.0;
            bmi = Math.round((request.getWeight() / (heightM * heightM)) * 10.0) / 10.0;
        }

        Progress progress = Progress.builder()
                .user(user)
                .weight(request.getWeight())
                .bmi(bmi)
                .bodyFatPercentage(request.getBodyFatPercentage())
                .recordedDate(request.getRecordedDate())
                .notes(request.getNotes())
                .build();

        return mapToResponse(progressRepository.save(progress));
    }

    @Override
    public List<ProgressResponse> getProgressHistory(Long userId) {
        return progressRepository.findByUserIdOrderByRecordedDateDesc(userId).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    private ProgressResponse mapToResponse(Progress progress) {
        ProgressResponse res = new ProgressResponse();
        res.setId(progress.getId());
        res.setWeight(progress.getWeight());
        res.setBmi(progress.getBmi());
        res.setBodyFatPercentage(progress.getBodyFatPercentage());
        res.setRecordedDate(progress.getRecordedDate());
        res.setNotes(progress.getNotes());
        return res;
    }
}

