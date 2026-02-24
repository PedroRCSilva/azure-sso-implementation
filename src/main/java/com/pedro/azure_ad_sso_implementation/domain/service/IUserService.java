package com.pedro.azure_ad_sso_implementation.domain.service;

import com.pedro.azure_ad_sso_implementation.domain.models.User;


public interface IUserService {

    User findByEmail(String email);
}
