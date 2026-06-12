package com.k41s.ecommerce_api.controllers;

import com.k41s.ecommerce_api.dtos.AuthenticatedUserDTO;
import com.k41s.ecommerce_api.dtos.LoginDTO;
import com.k41s.ecommerce_api.dtos.RefreshTokenRequestDTO;
import com.k41s.ecommerce_api.dtos.RegisterUserDTO;
import com.k41s.ecommerce_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        return ResponseEntity.ok(service.login(loginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserDTO> register(@Valid @RequestBody RegisterUserDTO registerDto) {
        AuthenticatedUserDTO authUserDto = service.registerAndLogin(registerDto);

        return new ResponseEntity<>(authUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticatedUserDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO requestDTO) {
        return ResponseEntity.ok(service.refreshAccessToken(requestDTO.getRefreshToken()));
    }
}
