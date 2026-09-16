package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.dtos.CartDTO;
import com.k41s.scrollspree_core.dtos.CartItemDTO;
import com.k41s.scrollspree_core.entities.Cart;
import com.k41s.scrollspree_core.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    CartDTO toDto(Cart cart);

    CartItemDTO toDto(CartItem cartItem);
}