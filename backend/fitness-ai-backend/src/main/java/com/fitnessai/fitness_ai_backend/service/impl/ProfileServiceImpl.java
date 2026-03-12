package com.fitnessai.fitness_ai_backend.service.impl;

import com.fitnessai.fitness_ai_backend.dto.request.ProfileRequest;
import com.fitnessai.fitness_ai_backend.dto.response.ProfileResponse;
import com.fitnessai.fitness_ai_backend.entity.User;
import com.fitnessai.fitness_ai_backend.entity.UserProfile;
import com.fitnessai.fitness_ai_backend.exception.ResourceNotFoundException;
import com.fitnessai.fitness_ai_backend.repository.UserProfileRepository;
import com.fitnessai.fitness_ai_backend.repository.UserRepository;
import com.fitnessai.fitness_ai_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse getProfile(Long userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));
        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        UserProfile profile = profileRepository.findByUserId(userId)
                .orElse(UserProfile.builder().user(user).build());

        profile.setAge(request.getAge());
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        profile.setGender(request.getGender());
        profile.setFitnessGoal(request.getFitnessGoal());
        profile.setActivityLevel(request.getActivityLevel());

        return mapToResponse(profileRepository.save(profile));
    }

    private ProfileResponse mapToResponse(UserProfile profile) {
        ProfileResponse res = new ProfileResponse();
        res.setId(profile.getId());
        res.setUserId(profile.getUser().getId());
        res.setUserName(profile.getUser().getName());
        res.setUserEmail(profile.getUser().getEmail());
        res.setAge(profile.getAge());
        res.setHeight(profile.getHeight());
        res.setWeight(profile.getWeight());
        res.setGender(profile.getGender());
        res.setFitnessGoal(profile.getFitnessGoal());
        res.setActivityLevel(profile.getActivityLevel());
        return res;
    }
}

