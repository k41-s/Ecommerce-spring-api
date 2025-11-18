package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ProductDTO;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.ProductMapper;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDTO getById(int id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));
    }

    public ProductDTO create(ProductDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    public void update(int id, ProductDTO updated) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
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
