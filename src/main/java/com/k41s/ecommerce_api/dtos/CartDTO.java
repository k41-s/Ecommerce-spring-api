package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Integer id;
    private Integer userId;
    private List<CartItemDTO> items;
}