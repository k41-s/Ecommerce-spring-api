package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND " +
            "(:search IS NULL OR p.name LIKE %:search%) AND " +
            "(:categoryId = 0 OR p.category.id = :categoryId)")
    Page<Product> search(
            @Param("search") String search,
            @Param("categoryId") Integer categoryId,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Product> findActiveById(@Param("id") Integer id);
}
