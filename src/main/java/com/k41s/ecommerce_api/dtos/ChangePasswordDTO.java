package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;

}
