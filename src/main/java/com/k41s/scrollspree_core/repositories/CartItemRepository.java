package com.k41s.scrollspree_core.repositories;

import com.k41s.scrollspree_core.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<Cart, Integer> { }