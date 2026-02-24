package com.pedro.azure_sso_implementation.infrastructure.mapper;

import com.pedro.azure_sso_implementation.domain.models.User;
import com.pedro.azure_sso_implementation.infrastructure.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity userEntity){
        return User.builder()
              .id(userEntity.getId())
              .name(userEntity.getName())
              .email(userEntity.getEmail())
              .createdAt(userEntity.getCreatedAt())
              .role(userEntity.getRole())
              .updatedAt(userEntity.getUpdatedAt())
              .build();
    }
}
