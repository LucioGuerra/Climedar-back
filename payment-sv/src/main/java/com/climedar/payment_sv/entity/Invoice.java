package com.climedar.payment_sv.entity;

import com.climedar.payment_sv.entity.payment.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false, updatable = false)
    private Long patientId;

    @Column(name = "total_amount", nullable = false, updatable = false)
    private BigDecimal TotalAmount;

    @Column(name = "invoice_date", nullable = false, updatable = false)
    private LocalDate invoiceDate;

    @ElementCollection
    @Column(name = "medical_service_id")
    private List<Long> medicalServicesId;

    @OneToOne
    private Payment payment;
}
