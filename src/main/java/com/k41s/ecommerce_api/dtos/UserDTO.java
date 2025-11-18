package com.k41s.ecommerce_api.dtos;

import com.k41s.ecommerce_api.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    public int id;
    public String email;
    public String username;
    public String name;
    public String surname;
    public String phone;
    public Role role;
}