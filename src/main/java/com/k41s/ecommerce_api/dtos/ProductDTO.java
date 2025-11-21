package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    public int id;
    public String name;
    public String description;
    public int categoryId;
    public String categoryName;
    public List<ProductImageDTO> images;
    public List<Integer> countryIds;
    public List<String> countryNames;
}