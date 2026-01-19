package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.CategoryDTO;
import com.k41s.ecommerce_api.entities.Category;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.CategoryMapper;
import com.k41s.ecommerce_api.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public List<CategoryDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CategoryDTO getById(int id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with ID " + id + " not found",
                        "CATEGORY_NOT_FOUND"
                ));
    }

    @PreAuthorize("hasRole('Admin')")
    public CategoryDTO create(CategoryDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    @PreAuthorize("hasRole('Admin')")
    public void update(int id, CategoryDTO updated) {
        Category existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with ID " + id + " not found",
                        "CATEGORY_NOT_FOUND"
                ));
        mapper.updateEntityFromDto(updated, existing);
        repository.save(existing);
    }

    @PreAuthorize("hasRole('Admin')")
    public boolean delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
