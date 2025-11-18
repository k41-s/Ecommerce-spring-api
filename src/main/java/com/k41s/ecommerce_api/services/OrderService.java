package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.OrderDTO;
import com.k41s.ecommerce_api.entities.Order;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.OrderMapper;
import com.k41s.ecommerce_api.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    public OrderService(OrderRepository repository, OrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrderDTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public OrderDTO getById(int id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with ID " + id + " not found",
                        "ORDER_NOT_FOUND"
                ));
    }

    public OrderDTO create(OrderDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    public void update(int id, OrderDTO updated) {
        Order existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with ID " + id + " not found",
                        "ORDER_NOT_FOUND"
                ));
        mapper.updateEntityFromDto(updated, existing);
        repository.save(existing);
    }

    public boolean delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
