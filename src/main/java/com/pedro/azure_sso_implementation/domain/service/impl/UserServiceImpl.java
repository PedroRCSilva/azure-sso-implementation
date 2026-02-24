package com.pedro.azure_sso_implementation.domain.service.impl;

import com.pedro.azure_sso_implementation.domain.exceptions.UserNotFoundException;
import com.pedro.azure_sso_implementation.domain.models.User;
import com.pedro.azure_sso_implementation.domain.persistence.IUserPersistence;
import com.pedro.azure_sso_implementation.domain.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserPersistence userPersistence;

    @Override
    public User findByEmail(String email) {
        var user = userPersistence.findByEmail(email);
        return user.orElseThrow(()-> new UserNotFoundException("Usuário não encontrado"));
    }
}
