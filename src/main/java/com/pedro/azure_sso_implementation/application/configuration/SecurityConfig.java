package com.pedro.azure_sso_implementation.application.configuration;

import com.pedro.azure_sso_implementation.domain.enums.UserRole;
import com.pedro.azure_sso_implementation.domain.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] URLS_PERMITIDAS = {
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/swagger-resources",
          "/swagger-resources/**",
          "/v3/api-docs/**",
          "/actuator/*",
          "/h2-console/**"
    };

    private final IUserService userPersistence;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
              .csrf(AbstractHttpConfigurer::disable)
              .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .headers(headers ->
                    headers.frameOptions(FrameOptionsConfig::sameOrigin))
              .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(
                          jwt->jwt.jwtAuthenticationConverter(new AuthDecoder(userPersistence))
                    )
              )
              .authorizeHttpRequests(auth -> auth
                    .requestMatchers(URLS_PERMITIDAS)
                    .permitAll()
                    .anyRequest().hasAuthority(UserRole.ADMIN.toString())
              );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}