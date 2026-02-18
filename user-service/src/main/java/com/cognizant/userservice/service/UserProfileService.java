package com.cognizant.userservice.service;

import com.cognizant.userservice.dto.UserProfileDTO;
import com.cognizant.userservice.dto.UserProfileResponse;

import java.util.List;

public interface UserProfileService {

    UserProfileResponse createUser(UserProfileDTO dto);

    List<UserProfileResponse> getAllUsers();

    UserProfileResponse getUserById(Long id);

    UserProfileResponse updateUser(Long id, UserProfileDTO dto);

    boolean deleteUser(Long id);

    boolean userExists(Long id); // for car-service validation
}