package com.climedar.payment_sv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "total_payments", nullable = false)
    private Long totalPayments;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_invoices", nullable = false)
    private RevenueType revenueType;
}
