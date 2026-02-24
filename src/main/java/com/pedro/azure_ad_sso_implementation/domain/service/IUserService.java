package com.pedro.azure_ad_sso_implementation.domain.service;

import com.pedro.azure_ad_sso_implementation.domain.models.User;
import org.springframework.stereotype.Service;


public interface IUserService {

    User findById(String email);
}
