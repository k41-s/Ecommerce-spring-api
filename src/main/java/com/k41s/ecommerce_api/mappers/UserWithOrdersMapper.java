package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.dtos.UserWithOrdersDTO;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface UserWithOrdersMapper {
    UserWithOrdersDTO toDto(User user);

    @BeforeMapping
    default void linkOrders(User user) {
        if (user.getOrders() != null) {
            user.getOrders().forEach(order -> {
                if (order.getUser() == null) {
                    order.setUser(user);
                }
            });
        }
    }

    @Mapping(target = "id", ignore = true)
    User toEntity(UserWithOrdersDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserWithOrdersDTO dto, @MappingTarget User entity);

}
