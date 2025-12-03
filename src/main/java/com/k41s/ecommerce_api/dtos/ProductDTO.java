package com.k41s.ecommerce_api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    public Integer id;
    public String name;
    public String description;
    public boolean isDeleted;
    public int categoryId;
    public String categoryName;
    public List<Integer> imageIds;
    public List<Integer> countryIds;
    public List<String> countryNames;
}