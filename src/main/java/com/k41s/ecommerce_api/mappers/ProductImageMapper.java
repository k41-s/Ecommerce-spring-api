package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.ProductImageDTO;
import com.k41s.ecommerce_api.entities.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImageDTO toDto(ProductImage entity);

    ProductImage toEntity(ProductImageDTO dto);
}
