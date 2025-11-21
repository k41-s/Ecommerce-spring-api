package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ProductDTO;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.entities.ProductImage;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.ProductImageMapper;
import com.k41s.ecommerce_api.mappers.ProductMapper;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper productMapper;
    private final ProductImageMapper imgMapper;

    public List<ProductDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDTO getById(int id) {
        return repository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));
    }

    public ProductDTO create(ProductDTO dto) {
        Product entity =  productMapper.toEntity(dto);
        if(dto.getImages() != null){
            List<ProductImage> images
                    = dto.getImages()
                    .stream()
                    .map(imgMapper::toEntity)
                    .toList();

            entity.setImages(images);
        }
        return productMapper.toDto(repository.save(entity));
    }

    public void update(int id, ProductDTO updated) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with ID " + id + " not found",
                        "PRODUCT_NOT_FOUND"
                ));
        productMapper.updateEntityFromDto(updated, existing);

        existing.getImages().clear();

        if(updated.getImages() != null){
            List<ProductImage> images
                    = updated.getImages()
                    .stream()
                    .map(imgMapper::toEntity)
                    .toList();

            existing.getImages().addAll(images);
        }

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
