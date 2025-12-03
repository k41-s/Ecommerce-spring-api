package com.k41s.ecommerce_api.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "Country")
public class Country extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "countries")
    private List<Product> products = new ArrayList<>();

}