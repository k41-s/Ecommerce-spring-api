package com.k41s.ecommerce_api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}