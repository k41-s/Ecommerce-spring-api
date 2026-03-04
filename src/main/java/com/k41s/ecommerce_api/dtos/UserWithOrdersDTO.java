package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserWithOrdersDTO {
    public String username;
    public String email;
    public String name;
    public String surname;
    public String phone;
    public Role role;
    public List<OrderDTO> orders;
}