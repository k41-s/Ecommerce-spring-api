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

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.orders o " +
            "JOIN FETCH o.product p " +
            "WHERE u.role != 'Admin' ")
    List<User> findAllWithOrders();
}