package com.k41s.ecommerce_api.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Product")
public class Product extends BaseEntity {
    private String name;
    private String description;
    private boolean isDeleted;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "ProductCountry",
            joinColumns = @JoinColumn(name = "ProductId"),
            inverseJoinColumns = @JoinColumn(name = "CountryId")
    )
    private List<Country> countries = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}