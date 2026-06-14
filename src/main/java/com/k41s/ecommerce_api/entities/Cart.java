package com.k41s.ecommerce_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Carts")
public class Cart extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();


    // --- Helper Methods for Bidirectional Synchronization ---

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCart(this);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        items.add(newItem);
    }

    public void removeItem(Product product) {
        items.removeIf(item -> {
            boolean matches = item.getProduct().getId().equals(product.getId());
            if (matches) {
                item.setCart(null);
            }
            return matches;
        });
    }

    public void clearCart() {
        for(CartItem item : items) {
            item.setCart(null);
        }
        items.clear();
    }
}