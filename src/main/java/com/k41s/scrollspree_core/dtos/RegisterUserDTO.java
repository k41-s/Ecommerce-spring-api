package com.k41s.scrollspree_core.dtos;

import com.k41s.scrollspree_core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserDTO {
    private String username;
    private String password;
    private String name;
    private String surname;
    private Role role;
    private String email;
    private String phone;
}

