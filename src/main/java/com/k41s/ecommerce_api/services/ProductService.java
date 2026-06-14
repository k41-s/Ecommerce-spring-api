package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ProductDTO;
import com.k41s.ecommerce_api.entities.Category;
import com.k41s.ecommerce_api.entities.Country;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.ProductMapper;
import com.k41s.ecommerce_api.repositories.CategoryRepository;
import com.k41s.ecommerce_api.repositories.CountryRepository;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final CountryRepository countryRepository;
    private final ProductMapper mapper;
    private final LogService logService;

    public Page<ProductDTO> getActiveProducts(String search, Integer categoryId, Pageable pageable) {
        String query = (search != null && !search.isBlank()) ? search : null;
        logService.log(LogLevel.Information, "Searching products with query: " + query + ", categoryId: " +  categoryId);

        return repository.search(query, categoryId, pageable)
                .map(mapper::toDto);
    }

    public ProductDTO getActiveProductById(int id) {
        ProductDTO dto = repository.findActiveById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));
        logService.log(LogLevel.Information, "Product with ID " + id + " found");
        return dto;
    }

    @Transactional
    @PreAuthorize("hasRole('Admin')")
    public void softDelete(Integer id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));

        if(!entity.isDeleted()){
            entity.setDeleted(true);
            repository.save(entity);
            logService.log(LogLevel.Information, "Product with ID " + id + " marked as deleted");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('Admin')")
    public ProductDTO create(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category Id"));

        List<Country> countries = countryRepository.findAllById(dto.getCountryIds());

        Product entity = mapper.toEntity(dto);

        entity.setCategory(category);
        entity.setCountries(countries);

        Product savedEntity = repository.save(entity);
        logService.log(LogLevel.Information, "Product with ID " + savedEntity.getId() + " created");

        return mapper.toDto(savedEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('Admin')")
    public void update(int id, ProductDTO updatedDto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));
        mapper.updateEntityFromDto(updatedDto, existing);

        if (existing.getCategory() == null
                || existing.getCategory().getId() != updatedDto.getCategoryId()) {
            Category newCategory = categoryRepository.findById(updatedDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Category Id"));

            existing.setCategory(newCategory);
        }

        if (updatedDto.getCountryIds() != null) {
            existing.getCountries().clear();
            List<Country> newCountries = countryRepository.findAllById(updatedDto.getCountryIds());
            existing.getCountries().addAll(newCountries);
        }

        repository.save(existing);
        logService.log(LogLevel.Information, "Product with ID " + id + " updated");
    }
}
