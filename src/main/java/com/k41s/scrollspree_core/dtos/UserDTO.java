package com.k41s.scrollspree_core.dtos;

import com.k41s.scrollspree_core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String email;
    private String username;
    private String name;
    private String surname;
    private String phone;
    private Role role;
}