package com.pedro.azure_sso_implementation.domain.persistence;

import com.pedro.azure_sso_implementation.domain.models.User;
import java.util.Optional;

public interface IUserPersistence {

    Optional<User> findByEmail(String email);
}
