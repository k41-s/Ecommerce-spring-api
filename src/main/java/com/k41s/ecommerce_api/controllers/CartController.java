package com.k41s.ecommerce_api.controllers;

import com.k41s.ecommerce_api.dtos.CartDTO;
import com.k41s.ecommerce_api.dtos.CartItemRequestDTO;
import com.k41s.ecommerce_api.security.CustomUserDetails;
import com.k41s.ecommerce_api.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCartForUser(userDetails.user().getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CartItemRequestDTO request) {

        return ResponseEntity.ok(cartService.addItemToCart(
                userDetails.user().getId(),
                request.getProductId(),
                request.getQuantity()
        ));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDTO> updateQuantity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer productId,
            @RequestBody CartItemRequestDTO request) {

        return ResponseEntity.ok(cartService.updateItemQuantity(
                userDetails.user().getId(),
                productId,
                request.getQuantity()
        ));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDTO> removeItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer productId) {

        return ResponseEntity.ok(cartService.removeItemFromCart(
                userDetails.user().getId(),
                productId
        ));
    }

    @DeleteMapping
    public ResponseEntity<CartDTO> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cartService.clearCart(userDetails.user().getId()));
    }
}