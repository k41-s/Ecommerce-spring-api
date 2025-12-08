package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.dtos.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { ProductImageMapper.class })
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "countryIds",
            expression = "java(entity.getCountries().stream().map(c -> c.getId()).toList())")
    @Mapping(target = "countryNames",
            expression = "java(entity.getCountries().stream().map(c -> c.getName()).toList())")
    @Mapping(target = "imageIds",
            expression = "java(entity.getImages().stream().map(i -> i.getId()).toList())")
    ProductDTO toDto(Product entity);

    // service handles images
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductDTO dto);


    @Mapping(target = "category", ignore = true)
    @Mapping(target = "countries", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductDTO dto, @MappingTarget Product entity);
}