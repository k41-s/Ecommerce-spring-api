package com.k41s.scrollspree_core.entities;

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
