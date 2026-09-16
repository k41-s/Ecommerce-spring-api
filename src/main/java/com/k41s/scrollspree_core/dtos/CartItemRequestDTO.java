package com.k41s.scrollspree_core.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDTO {
    private Integer productId;
    private int quantity;
}