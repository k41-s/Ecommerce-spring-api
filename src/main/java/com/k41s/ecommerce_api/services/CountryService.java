package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.CountryDTO;
import com.k41s.ecommerce_api.entities.Country;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.CountryMapper;
import com.k41s.ecommerce_api.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public CountryDTO create(CountryDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    public void update(int id, CountryDTO updated) {
        Country existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Country with ID " + id + " not found",
                        "COUNTRY_NOT_FOUND"
                ));
        mapper.updateEntityFromDto(updated, existing);
        repository.save(existing);
    }

    public boolean delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
