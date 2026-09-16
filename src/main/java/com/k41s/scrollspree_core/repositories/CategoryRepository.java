package com.k41s.scrollspree_core.repositories;

import com.k41s.scrollspree_core.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
