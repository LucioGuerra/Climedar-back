package com.climedar.payment_sv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    private Double TotalAmount;

    private LocalDateTime invoiceDate;

    @OneToOne
    private Payment payment;
}
