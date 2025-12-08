package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.CountryDTO;
import com.k41s.ecommerce_api.entities.Country;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.CountryMapper;
import com.k41s.ecommerce_api.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository repository;
    private final CountryMapper mapper;

    public List<CountryDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CountryDTO getById(int id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country with ID " + id + " not found",
                        "COUNTRY_NOT_FOUND"
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CountryDTO create(CountryDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void update(int id, CountryDTO updated) {
        Country existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country with ID " + id + " not found",
                        "COUNTRY_NOT_FOUND"
                ));
        mapper.updateEntityFromDto(updated, existing);
        repository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public boolean delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
