package com.k41s.ecommerce_api.repositories;

import com.k41s.ecommerce_api.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Integer> {
}
