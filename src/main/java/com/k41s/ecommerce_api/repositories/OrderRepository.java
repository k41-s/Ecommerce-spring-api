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
    @Query("SELECT new com.k41s.ecommerce_api.dtos.OrderDTO(" +
            "o.id, " +
            "p.id, " +
            "p.name, " +
            "p.isDeleted, " +
            "u.id, " +
            "CONCAT(u.name, ' ', u.surname), " +
            "o.orderedAt, " +
            "o.paymentMethod, " +
            "o.notes, " +
            " (SELECT pi.id FROM ProductImage pi WHERE pi.product.id = p.id ORDER BY pi.id ASC LIMIT 1) " +
            ") FROM Order o " +
            "JOIN o.product p " +
            "JOIN o.user u " +
            "ORDER BY o.orderedAt DESC")
    List<OrderDTO> findAllOrdersProjected();

    @Query("SELECT new com.k41s.ecommerce_api.dtos.OrderDTO(" +
            "o.id, " +
            "p.id, " +
            "p.name, " +
            "p.isDeleted, " +
            "u.id, " +
            "CONCAT(u.name, ' ', u.surname), " +
            "o.orderedAt, " +
            "o.paymentMethod, " +
            "o.notes, " +
            " (SELECT pi.id FROM ProductImage pi WHERE pi.product.id = p.id ORDER BY pi.id ASC LIMIT 1) " +
            ") FROM Order o " +
            "JOIN o.product p " +
            "JOIN o.user u " +
            "WHERE o.user.id = :userId " +
            "ORDER BY o.orderedAt DESC")
    List<OrderDTO> findUserOrdersProjected(@Param("userId") Integer userId);

}
