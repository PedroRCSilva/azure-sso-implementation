package com.pedro.azure_ad_sso_implementation.domain.models;

import com.pedro.azure_ad_sso_implementation.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    private String email;

    @Setter
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserRole role;

}