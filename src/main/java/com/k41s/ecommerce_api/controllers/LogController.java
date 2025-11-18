package com.k41s.ecommerce_api.controllers;

import com.k41s.ecommerce_api.entities.Log;
import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    public void log(LogLevel level, String message) {
        service.log(level, message);
    }

    @GetMapping
    public List<Log> getAll() {
        return service.getAll();
    }
}
