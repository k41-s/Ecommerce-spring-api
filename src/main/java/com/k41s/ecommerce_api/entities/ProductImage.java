package com.k41s.ecommerce_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ProductImage")
public class ProductImage extends BaseEntity {

    @Lob
    private byte[] data;

    private String mimeType;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;
}
