package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.dtos.UserWithOrdersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface UserWithOrdersMapper {
    UserWithOrdersDTO toDto(User user);
    User toEntity(UserWithOrdersDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserWithOrdersDTO dto, @MappingTarget User entity);

}
