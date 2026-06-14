package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDTO {
    private Integer productId;
    private int quantity;
}