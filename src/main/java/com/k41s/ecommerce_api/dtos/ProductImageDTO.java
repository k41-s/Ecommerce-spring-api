package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDTO {
    private Integer id;
    private String url;
    private String mimeType;
}
