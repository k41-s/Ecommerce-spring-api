package com.k41s.ecommerce_api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    public BigDecimal price;

    private boolean isDeleted;
    private int categoryId;
    private String categoryName;
    private List<Integer> imageIds;
    private List<Integer> countryIds;
    private List<String> countryNames;
}