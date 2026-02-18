package com.cognizant.userservice.service;

import com.cognizant.userservice.dto.UserProfileDTO;
import com.cognizant.userservice.dto.UserProfileResponse;
import com.cognizant.userservice.exception.DuplicateResourceException;
import com.cognizant.userservice.exception.ResourceNotFoundException;
import com.cognizant.userservice.model.UserProfile;
import com.cognizant.userservice.repository.UserProfileRepository;
import com.cognizant.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository repository;

    @Override
    public UserProfileResponse createUser(UserProfileDTO dto) {

        if (repository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        UserProfile user = UserProfile.builder()
                .username(dto.getUsername())
                .role(dto.getRole())
                .preference(dto.getPreference())
                .build();

        UserProfile saved = repository.save(user);
        return mapToResponse(saved);
    }

    @Override
    public List<UserProfileResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserProfileResponse getUserById(Long id) {
        UserProfile user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public UserProfileResponse updateUser(Long id, UserProfileDTO dto) {

        UserProfile existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existing.setRole(dto.getRole());
        existing.setPreference(dto.getPreference());

        UserProfile updated = repository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public boolean deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean userExists(Long id) {
        return repository.existsById(id);
    }

    // Used Mapper
    private UserProfileResponse mapToResponse(UserProfile user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getPreference(),
                user.getCreatedAt()
        );
    }
}