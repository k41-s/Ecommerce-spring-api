package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.OrderDTO;
import com.k41s.ecommerce_api.dtos.OrderItemDTO;
import com.k41s.ecommerce_api.entities.Order;
import com.k41s.ecommerce_api.entities.OrderItem;
import com.k41s.ecommerce_api.entities.Product;
import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.exceptions.ProductOrderException;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.OrderMapper;
import com.k41s.ecommerce_api.repositories.OrderRepository;
import com.k41s.ecommerce_api.repositories.ProductRepository;
import com.k41s.ecommerce_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper mapper;

    @PreAuthorize("hasRole('Admin')")
    public List<OrderDTO> getAll() {
        return repository.findAllWithItems()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getUserOrders(Integer userId) {
        List<Order> orders = repository.findByUserIdWithItems(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user id: " + userId, "ORDER_NOT_FOUND");
        }
        return orders.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public OrderDTO create(OrderDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = mapper.toEntity(dto);
        order.setUser(user);
        order.setOrderedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        if (dto.getItems() != null) {
            for (OrderItemDTO itemDto : dto.getItems()) {
                Product product = checkProductValidity(itemDto.getProductId());

                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(itemDto.getQuantity());
                item.setOrder(order);

                orderItems.add(item);
            }
        }
        order.setItems(new HashSet<>(orderItems));

        Order savedOrder = repository.save(order);
        return mapper.toDto(savedOrder);
    }

    public List<OrderDTO> getUserOrdersByDateRange(Integer userId, LocalDateTime start, LocalDateTime end) {
        List<Order> orders = repository.findByUserIdAndOrderedAtBetweenWithItems(userId, start, end);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user id: " +
                    userId + " in the specified date range.", "ORDER_NOT_FOUND");
        }

        return orders.stream().map(mapper::toDto).collect(Collectors.toList());
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
