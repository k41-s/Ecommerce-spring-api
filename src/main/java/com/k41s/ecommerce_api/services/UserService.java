package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.UserDTO;
import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.mappers.UserMapper;
import com.k41s.ecommerce_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public UserDTO getById(int id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        "USER_NOT_FOUND"
                ));
    }

    public UserDTO create(UserDTO dto) {
        return mapper.toDto(
                repository.save(mapper.toEntity(dto))
        );
    }

    public void update(int id, UserDTO updated) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID " + id + " not found",
                        "USER_NOT_FOUND"
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
