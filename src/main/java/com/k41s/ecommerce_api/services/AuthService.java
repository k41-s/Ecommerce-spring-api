package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.AuthenticatedUserDTO;
import com.k41s.ecommerce_api.dtos.LoginDTO;
import com.k41s.ecommerce_api.dtos.RegisterUserDTO;
import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.enums.Role;
import com.k41s.ecommerce_api.exceptions.UserValidationException;
import com.k41s.ecommerce_api.mappers.UserMapper;
import com.k41s.ecommerce_api.repositories.UserRepository;
import com.k41s.ecommerce_api.security.CustomUserDetails;
import com.k41s.ecommerce_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticatedUserDTO login(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return generateTokenAndMapDTO(authentication);
    }

    public AuthenticatedUserDTO registerAndLogin(RegisterUserDTO dto) {
        validateRegistrationCredentials(dto);

        User user = mapper.fromRegisterDto(dto);
        String rawPassword = dto.getPassword();
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(Role.User);
        User savedUser = userRepository.save(user);
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return generateTokenAndMapDTO(authentication);
    }

    private AuthenticatedUserDTO generateTokenAndMapDTO(Authentication authentication) {
        String token = tokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.user();
        AuthenticatedUserDTO authUserDto = mapper.toAuthenticatedUserDto(user);
        authUserDto.setToken(token);

        return authUserDto;
    }

    private void validateRegistrationCredentials(RegisterUserDTO dto) {
        if (
                userRepository.existsByUsername(dto.getUsername())
                        || userRepository.existsByEmail(dto.getEmail())
        ) {
            throw new UserValidationException("Username or email already exists");
        }

    }
}
