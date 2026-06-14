package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.AuthenticatedUserDTO;
import com.k41s.ecommerce_api.dtos.LoginDTO;
import com.k41s.ecommerce_api.dtos.RegisterUserDTO;
import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.entities.Cart;
import com.k41s.ecommerce_api.enums.Role;
import com.k41s.ecommerce_api.exceptions.JwtMalformedException;
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
        user.setRole(Role.USER);

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

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

    public AuthenticatedUserDTO refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new JwtMalformedException("Invalid refresh token");
        }

        String tokenType = tokenProvider.getTokenTypeFromJWT(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new JwtMalformedException("Provided token is not a refresh token");
        }

        String username = tokenProvider.getUsernameFromJWT(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserValidationException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return generateTokenAndMapDTO(authentication);
    }

    private AuthenticatedUserDTO generateTokenAndMapDTO(Authentication authentication) {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.user();
        AuthenticatedUserDTO authUserDto = mapper.toAuthenticatedUserDto(user);
        authUserDto.setToken(accessToken);
        authUserDto.setRefreshToken(refreshToken);

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
