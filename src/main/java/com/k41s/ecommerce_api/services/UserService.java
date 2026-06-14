package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.dtos.ChangePasswordDTO;
import com.k41s.ecommerce_api.dtos.UserDTO;
import com.k41s.ecommerce_api.dtos.UserWithOrdersDTO;
import com.k41s.ecommerce_api.entities.User;
import com.k41s.ecommerce_api.exceptions.ResourceNotFoundException;
import com.k41s.ecommerce_api.exceptions.UserValidationException;
import com.k41s.ecommerce_api.mappers.UserMapper;
import com.k41s.ecommerce_api.mappers.UserWithOrdersMapper;
import com.k41s.ecommerce_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final UserWithOrdersMapper userWithOrdersMapperMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String NOT_FOUND = " not found";
    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String USER_WITH_ID = "User with ID ";

    @PreAuthorize("hasRole('Admin')")
    public List<UserDTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDTO getById(int id) {
        return repository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        USER_WITH_ID + id + NOT_FOUND,
                        USER_NOT_FOUND
                ));
    }

    public UserDTO getByEmail(String email) {
        return repository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email " + email + NOT_FOUND,
                        USER_NOT_FOUND
                ));
    }

    @Transactional
    public void update(int id, UserDTO updated) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        USER_WITH_ID + id + NOT_FOUND,
                        USER_NOT_FOUND
                ));

        userMapper.updateEntityFromDto(updated, existing);
        repository.save(existing);
    }

    @Transactional
    public void updateProfileByEmail(String email, UserDTO dto) {
        User existing = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email " + email + NOT_FOUND,
                        USER_NOT_FOUND
                ));
        userMapper.updateEntityFromDto(dto, existing);
        repository.save(existing);
    }

    @PreAuthorize("hasRole('Admin')")
    public boolean delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteProfile(Integer userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        USER_WITH_ID + userId + NOT_FOUND,
                        USER_NOT_FOUND
                ));
        repository.delete(user);
    }

    @PreAuthorize("hasRole('Admin')")
    @Transactional(readOnly = true)
    public List<UserWithOrdersDTO> getUsersWithOrders() {
        return repository.findAllWithOrders()
                .stream()
                .map(userWithOrdersMapperMapper::toDto)
                .toList();
    }

    public void changePassword(ChangePasswordDTO dto) {
        User user = findUserOrThrow(dto.getUsername());
        boolean isOldPasswordCorrect
                = passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash());
        if (!isOldPasswordCorrect) {
            throw new UserValidationException("Invalid credentials: Incorrect old password.");
        }
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);
    }

    private User findUserOrThrow(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserValidationException(
                        "Invalid credentials: User not found."
                ));
    }
}
