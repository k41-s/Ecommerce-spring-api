package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<Cart, Integer> { }