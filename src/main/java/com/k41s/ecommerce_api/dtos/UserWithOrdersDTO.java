package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.Role;
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