package com.k41s.ecommerce_api.controllers;

import com.k41s.ecommerce_api.dtos.ChangePasswordDTO;
import com.k41s.ecommerce_api.dtos.UserDTO;
import com.k41s.ecommerce_api.dtos.UserWithOrdersDTO;
import com.k41s.ecommerce_api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @GetMapping("/with-orders")
    public ResponseEntity<List<UserWithOrdersDTO>> getUsersWithOrders() {
        return ResponseEntity.ok(service.getUsersWithOrders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody UserDTO dto) {
        service.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profile/{email}")
    public ResponseEntity<Void> updateProfileByEmail(@PathVariable String email, @Valid @RequestBody UserDTO dto) {
        service.updateProfileByEmail(email, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        service.changePassword(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
