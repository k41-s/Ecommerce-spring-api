package com.k41s.scrollspree_core.entities;
import com.k41s.scrollspree_core.enums.LogLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Log")
public class Log extends BaseEntity {
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private LogLevel level;

    @Column(columnDefinition = "TEXT")
    private String message;

}
