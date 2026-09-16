package com.k41s.scrollspree_core.services;

import com.k41s.scrollspree_core.dtos.CartDTO;
import com.k41s.scrollspree_core.entities.Cart;
import com.k41s.scrollspree_core.entities.CartItem;
import com.k41s.scrollspree_core.entities.Product;
import com.k41s.scrollspree_core.entities.User;
import com.k41s.scrollspree_core.exceptions.BadRequestException;
import com.k41s.scrollspree_core.exceptions.ResourceNotFoundException;
import com.k41s.scrollspree_core.mappers.CartMapper;
import com.k41s.scrollspree_core.repositories.CartRepository;
import com.k41s.scrollspree_core.repositories.ProductRepository;
import com.k41s.scrollspree_core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    private CartService self;
    @Autowired
    public void setSelf(@Lazy CartService self) {
        this.self = self;
    }

    private Cart getOrCreateCart(Integer userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found", "USER_NOT_FOUND"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional(readOnly = true)
    public CartDTO getCartForUser(Integer userId) {
        return cartMapper.toDto(getOrCreateCart(userId));
    }

    @Transactional
    public CartDTO addItemToCart(Integer userId, Integer productId, int quantity) {
        if (quantity <= 0) throw new BadRequestException("Quantity must be greater than zero", "INVALD_QUANTITY");

        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "PRODUCT_NOT_FOUND"));

        if (product.isDeleted()) {
            throw new BadRequestException("Cannot add a deleted product to cart", "PRODUCT_DELETED");
        }

        cart.addItem(product, quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDTO updateItemQuantity(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        if (quantity <= 0) {
            return self.removeItemFromCart(userId, productId);
        }

        boolean found = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }

        if (!found) throw new ResourceNotFoundException("Item not found in cart", "ITEM_NOT_FOUND");

        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDTO removeItemFromCart(Integer userId, Integer productId) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "PRODUCT_NOT_FOUND"));

        cart.removeItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartDTO clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cart.clearCart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }
}