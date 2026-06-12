package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.dtos.OrderDTO;
import com.k41s.ecommerce_api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product LEFT JOIN FETCH o.user")
    List<Order> findAllWithItems();

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.user.id = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") Integer userId);

}
