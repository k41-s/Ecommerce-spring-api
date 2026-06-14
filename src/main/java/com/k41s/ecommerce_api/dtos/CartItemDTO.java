package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Integer id;
    private ProductDTO product;
    private int quantity;
}
