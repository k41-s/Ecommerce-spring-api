package com.k41s.ecommerce_api.services;

import com.k41s.ecommerce_api.entities.Log;
import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.repositories.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(LogLevel level, String message) {
        Log log = new Log();

        log.setTimestamp(LocalDateTime.now(Clock.systemDefaultZone()));
        log.setLevel(level);
        log.setMessage(message);

        repo.save(log);
    }

    @PreAuthorize("hasRole('Admin')")
    public List<Log> getAll() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}
