package com.pulse.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "circulation_id", nullable = false)
    private Circulation circulation;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private FineStatus status;

    private LocalDateTime issuedDate;
    private LocalDateTime paidDate;
    private String paymentMethod;
    private String notes;
} 