package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.ProductImageDTO;
import com.k41s.ecommerce_api.entities.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "url", ignore = true)
    ProductImageDTO toDto(ProductImage entity);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "data",  ignore = true)
    ProductImage toEntity(ProductImageDTO dto);
}