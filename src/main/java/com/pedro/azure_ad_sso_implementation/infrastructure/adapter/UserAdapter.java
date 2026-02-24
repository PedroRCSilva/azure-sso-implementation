package com.pedro.azure_ad_sso_implementation.infrastructure.adapter;

import com.pedro.azure_ad_sso_implementation.domain.models.User;
import com.pedro.azure_ad_sso_implementation.domain.persistence.IUserPersistence;
import com.pedro.azure_ad_sso_implementation.infrastructure.mapper.UserMapper;
import com.pedro.azure_ad_sso_implementation.infrastructure.repository.IUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements IUserPersistence {

    private final IUserRepository userRepository;


    @Override
    public Optional<User> findByEmail(String email) {
        var user = userRepository.findByEmail(email);
        return user.map(UserMapper::toDomain);
    }
}
