package com.k41s.ecommerce_api.entities;

import com.k41s.ecommerce_api.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "CustomerOrder")
public class Order extends BaseEntity {

    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

}