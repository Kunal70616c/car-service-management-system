package com.cognizant.userservice.dto;

import com.cognizant.userservice.model.Preference;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String username,
        String role,
        Preference preference,
        LocalDateTime createdAt
) {}