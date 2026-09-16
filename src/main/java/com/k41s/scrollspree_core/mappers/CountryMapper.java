package com.k41s.scrollspree_core.mappers;

import com.k41s.scrollspree_core.dtos.CountryDTO;
import com.k41s.scrollspree_core.entities.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO toDto(Country country);

    @Mapping(target = "id", ignore = true)
    Country toEntity(CountryDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CountryDTO dto, @MappingTarget Country entity);

}
