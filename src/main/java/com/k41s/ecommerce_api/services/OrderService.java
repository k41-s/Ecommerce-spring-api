package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.OrderDTO;
import com.k41s.ecommerce_api.entities.Order;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.exceptions.ProductOrderException;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.OrderMapper;
import com.k41s.ecommerce_api.repositories.OrderRepository;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAll() {
        return repository.findAllOrdersProjected();
    }

    public List<OrderDTO> getUserOrders(Integer userId) {
        List<OrderDTO> orderDTOs = repository.findUserOrdersProjected(userId);
        if (orderDTOs.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No orders found for user id: " + userId,
                    "ORDER_NOT_FOUND"
            );
        }
        return orderDTOs;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO create(OrderDTO dto) {
        Product product = checkProductValidity(dto.getProductId());
        Order order = mapper.toEntity(dto);
        order.setProduct(product);
        Order savedOrder = repository.save(order);
        return mapper.toDto(savedOrder);
    }

    private Product checkProductValidity(Integer productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ProductOrderException(
                    "Product not found with ID: " + productId
            );
        }

        Product product = productOpt.get();
        if (product.isDeleted()) {
            throw new ProductOrderException(
                    "Product is deleted and cannot be ordered: " + productId
            );
        }
        return product;
    }
}
