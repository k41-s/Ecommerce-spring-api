package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log,Integer> {
}
