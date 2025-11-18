package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.AuthenticatedUserDTO;
import com.k41s.ecommerce_api.dtos.RegisterUserDTO;
import com.k41s.ecommerce_api.dtos.UserDTO;
import com.k41s.ecommerce_api.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);
    User toEntity(UserDTO dto);

    AuthenticatedUserDTO toAuthenticatedUserDto(User user);
    User fromAuthenticatedUserDto(AuthenticatedUserDTO dto);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "User")
    User fromRegisterDto(RegisterUserDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);


}
