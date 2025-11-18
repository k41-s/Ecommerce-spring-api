package com.k41s.ecommerce_api.mappers;

import com.k41s.ecommerce_api.dtos.CategoryDTO;
import com.k41s.ecommerce_api.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category category);
    Category toEntity(CategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CategoryDTO dto, @MappingTarget Category entity);

}
