package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
