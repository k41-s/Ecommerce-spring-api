package com.k41s.ecommerce_api.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.k41s.ecommerce_api.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "CustomerOrder")
public class Order extends BaseEntity {

    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

}