package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.entities.User;
import com.k41s.scrollspree_core.dtos.UserWithOrdersDTO;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
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

    User toEntity(UserWithOrdersDTO dto);

    void updateEntityFromDto(UserWithOrdersDTO dto, @MappingTarget User entity);

}
