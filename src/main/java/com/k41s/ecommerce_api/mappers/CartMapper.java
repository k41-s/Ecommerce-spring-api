package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.CartDTO;
import com.k41s.ecommerce_api.dtos.CartItemDTO;
import com.k41s.ecommerce_api.entities.Cart;
import com.k41s.ecommerce_api.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    CartDTO toDto(Cart cart);

    CartItemDTO toDto(CartItem cartItem);
}