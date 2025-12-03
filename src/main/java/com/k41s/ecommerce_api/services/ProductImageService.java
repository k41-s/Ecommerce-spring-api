package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ProductImageDTO;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.entities.ProductImage;
import com.k41s.ecommerce_api.exceptions.ImageProcessingException;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.ProductImageMapper;
import com.k41s.ecommerce_api.repositories.ProductImageRepository;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ProductImageService {
    private final ProductImageRepository repository;
    private final ProductRepository productRepository;
    private final ProductImageMapper mapper;

    public ProductImage getImageEntityById(int id) {
        ProductImage image = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with ID " + id + " not found",
                        "IMAGE_NOT_FOUND"
                ));
        if (image.getMimeType() == null || image.getMimeType().trim().isEmpty()) {
            image.setMimeType("image/png");
        }
        return image;
    }

    public ProductImageDTO uploadImage(int productId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded.");
        }

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product with ID " + productId + " not found",
                            "PRODUCT_NOT_FOUND"
                    ));
            ProductImage productImage = getProductImage(file, product);
            ProductImage savedImage = repository.save(productImage);
            String url = getUrl(savedImage);

            ProductImageDTO dto = mapper.toDto(savedImage);
            dto.setUrl(url);
            return dto;
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to read the contents of the uploaded file.", e);
        }
    }

    private static ProductImage getProductImage(MultipartFile file, Product product) throws IOException {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setData(file.getBytes());
        productImage.setMimeType(file.getContentType());
        return productImage;
    }

    private static String getUrl(ProductImage savedImage) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/productimages/")
                .path(String.valueOf(savedImage.getId()))
                .toUriString();
    }

    public boolean deleteImage(int id) {
        if (repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public ProductImageDTO getImageMetaById(int id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with ID " + id + " not found",
                        "IMAGE_NOT_FOUND"
                ));
    }
}
