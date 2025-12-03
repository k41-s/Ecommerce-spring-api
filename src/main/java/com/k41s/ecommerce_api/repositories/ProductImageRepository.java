package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
}
