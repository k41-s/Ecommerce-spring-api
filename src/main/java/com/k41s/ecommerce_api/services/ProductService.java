package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ProductDTO;
import com.k41s.ecommerce_api.entities.Category;
import com.k41s.ecommerce_api.entities.Country;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.ProductMapper;
import com.k41s.ecommerce_api.repositories.CategoryRepository;
import com.k41s.ecommerce_api.repositories.CountryRepository;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final CountryRepository countryRepository;
    private final ProductMapper mapper;

    public Page<ProductDTO> getActiveProducts(String search, Integer categoryId, Pageable pageable) {
        String query = (search != null && !search.isBlank()) ? search : null;
        log.info("Searching products with query: '{}', categoryId: {}", query, categoryId);

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
        log.info("Product with id={} retrieved", id);
        return dto;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void softDelete(Integer id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));

        if(!entity.isDeleted()){
            entity.setDeleted(true);
            repository.save(entity);
            log.info("Product with id={} marked as deleted", id);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO create(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category Id"));

        List<Country> countries = countryRepository.findAllById(dto.getCountryIds());

        Product entity = mapper.toEntity(dto);

        entity.setCategory(category);
        entity.setCountries(countries);

        Product savedEntity = repository.save(entity);
        log.info("Product with id={} created", savedEntity.getId());

        return mapper.toDto(savedEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
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
        log.info("Product with id={} updated", id);
    }
}
