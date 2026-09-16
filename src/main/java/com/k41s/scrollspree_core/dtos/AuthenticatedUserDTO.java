package com.k41s.scrollspree_core.dtos;

import com.k41s.scrollspree_core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticatedUserDTO {
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private Role role;
    private String name;
    private String surname;
    private String phone;
}
