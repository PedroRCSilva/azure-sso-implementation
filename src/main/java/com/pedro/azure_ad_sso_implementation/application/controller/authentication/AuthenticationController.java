package com.pedro.azure_ad_sso_implementation.application.controller.authentication;

import com.pedro.azure_ad_sso_implementation.application.controller.authentication.dto.AuthenticationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @GetMapping
    public ResponseEntity<AuthenticationResponseDTO> login(){
        return ResponseEntity.ok().body(new AuthenticationResponseDTO());
    }


}
