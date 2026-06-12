package com.k41s.ecommerce_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "OrderItem")
public class OrderItem extends BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "OrderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;
}