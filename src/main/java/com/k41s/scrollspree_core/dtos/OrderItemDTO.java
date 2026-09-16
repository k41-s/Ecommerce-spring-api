package com.k41s.scrollspree_core.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private Integer mainImgId;
    private Boolean isProductDeleted;
}