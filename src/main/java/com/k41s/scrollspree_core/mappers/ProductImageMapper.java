package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.dtos.ProductImageDTO;
import com.k41s.scrollspree_core.entities.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "url", ignore = true)
    ProductImageDTO toDto(ProductImage entity);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "data",  ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductImage toEntity(ProductImageDTO dto);
}