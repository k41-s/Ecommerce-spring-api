package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticatedUserDTO {
    private String token;
    private String username;
    private String email;
    private Role role;
    private String name;
    private String surname;
    private String phone;

}
