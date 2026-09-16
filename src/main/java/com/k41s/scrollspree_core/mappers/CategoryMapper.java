package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.dtos.CategoryDTO;
import com.k41s.scrollspree_core.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CategoryDTO dto, @MappingTarget Category entity);

}
