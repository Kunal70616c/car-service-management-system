package com.cognizant.userservice.model;

import com.cognizant.userservice.config.PreferenceConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Role is required")
    private String role;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = PreferenceConverter.class)
    private Preference preference;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}