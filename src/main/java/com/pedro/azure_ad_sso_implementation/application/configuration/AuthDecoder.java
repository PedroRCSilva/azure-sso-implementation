package com.pedro.azure_ad_sso_implementation.application.configuration;

import com.pedro.azure_ad_sso_implementation.domain.service.IUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@RequiredArgsConstructor
public class AuthDecoder implements Converter<Jwt, AbstractAuthenticationToken> {

    private final IUserService userService;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var email = source.getClaim("preferred_username");
        var user = userService.findByEmail((String) email);
        var authorities = new SimpleGrantedAuthority(user.getRole().toString());
        return new JwtAuthenticationToken(source,List.of(authorities));
    }
}
