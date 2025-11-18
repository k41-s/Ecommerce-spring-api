package com.k41s.ecommerce_api.entities;
import com.k41s.ecommerce_api.enums.LogLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private LogLevel level;

    @Column(columnDefinition = "nvarchar(max)")
    private String message;

}
