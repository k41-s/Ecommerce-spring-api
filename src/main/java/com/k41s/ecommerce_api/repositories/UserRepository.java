package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.orders o " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.product " +
            "WHERE u.role != 'Admin' ")
    List<User> findAllWithOrders();
}