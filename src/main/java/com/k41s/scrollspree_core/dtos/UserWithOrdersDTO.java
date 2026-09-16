package com.k41s.scrollspree_core.dtos;

import com.k41s.scrollspree_core.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserWithOrdersDTO {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private Role role;
    private List<OrderDTO> orders;
}