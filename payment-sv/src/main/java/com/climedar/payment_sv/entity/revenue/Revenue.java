package com.climedar.payment_sv.entity.revenue;

import com.climedar.payment_sv.external.model.medical_services.ServicesType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "speciality_name", nullable = false)
    private String specialityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "medical_services_type", nullable = false)
    private ServicesType medicalServicesType;

    @Column(name = "total_payments", nullable = false)
    private Long totalPayments;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_invoices", nullable = false)
    private RevenueType revenueType;
}
