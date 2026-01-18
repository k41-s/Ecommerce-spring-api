package com.k41s.ecommerce_api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDTO {
    public Integer id;
    public String name;
    public String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    public BigDecimal price;

    public boolean isDeleted;
    public int categoryId;
    public String categoryName;
    public List<Integer> imageIds;
    public List<Integer> countryIds;
    public List<String> countryNames;
}