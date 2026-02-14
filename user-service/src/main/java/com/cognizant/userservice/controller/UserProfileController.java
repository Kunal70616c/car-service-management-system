package com.cognizant.userservice.controller;

import com.cognizant.userservice.dto.GenericResponse;
import com.cognizant.userservice.dto.UserProfileDTO;
import com.cognizant.userservice.dto.UserProfileResponse;
import com.cognizant.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    // CREATE USER
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_admin','SCOPE_user')")
    public ResponseEntity<GenericResponse<UserProfileResponse>> createUser(
            @Valid @RequestBody UserProfileDTO dto) {

        UserProfileResponse created = service.createUser(dto);

        return ResponseEntity.status(201)
                .body(new GenericResponse<>("User created successfully", created));
    }

    // GET ALL USERS (ADMIN ONLY)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<GenericResponse<List<UserProfileResponse>>> getAllUsers() {

        List<UserProfileResponse> users = service.getAllUsers();

        return ResponseEntity.ok(
                new GenericResponse<>("Users fetched successfully", users)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin','SCOPE_user')")
    public ResponseEntity<GenericResponse<UserProfileResponse>> getUserById(@PathVariable Long id) {

        UserProfileResponse user = service.getUserById(id);

        return ResponseEntity.ok(
                new GenericResponse<>("User fetched", user)
        );
    }

    // UPDATE (ADMIN ONLY)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<GenericResponse<UserProfileResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileDTO dto) {

        UserProfileResponse updated = service.updateUser(id, dto);

        return ResponseEntity.ok(
                new GenericResponse<>("User updated successfully", updated)
        );
    }

    // DELETE (ADMIN ONLY)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<GenericResponse<String>> deleteUser(@PathVariable Long id) {

        service.deleteUser(id);

        return ResponseEntity.status(202)
                .body(new GenericResponse<>("User deleted successfully"));
    }

    // ⭐ INTERNAL EXISTS API (NO AUTH for service-to-service)
    @PreAuthorize("hasAuthority('SCOPE_internal_service')")
    @GetMapping("/exists/{id}")
    public ResponseEntity<GenericResponse<Boolean>> userExists(@PathVariable Long id) {
        return ResponseEntity.ok(
                new GenericResponse<>("User existence checked", service.userExists(id))
        );
    }
}