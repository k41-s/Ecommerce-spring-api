package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.dtos.AuthenticatedUserDTO;
import com.k41s.scrollspree_core.dtos.RegisterUserDTO;
import com.k41s.scrollspree_core.dtos.UserDTO;
import com.k41s.scrollspree_core.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthenticatedUserDTO toAuthenticatedUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    User fromAuthenticatedUserDto(AuthenticatedUserDTO dto);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "id", ignore = true)
    User fromRegisterDto(RegisterUserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User entity);
}
