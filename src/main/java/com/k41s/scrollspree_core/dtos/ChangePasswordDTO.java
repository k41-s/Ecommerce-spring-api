package com.k41s.scrollspree_core.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;

}
