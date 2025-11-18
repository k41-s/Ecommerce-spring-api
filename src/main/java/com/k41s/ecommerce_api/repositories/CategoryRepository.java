package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
