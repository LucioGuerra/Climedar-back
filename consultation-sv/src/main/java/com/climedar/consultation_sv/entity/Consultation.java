package com.climedar.consultation_sv.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Double finalPrice;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 3000)
    private String observation;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @ElementCollection
    @Column(name = "medical_service_code", nullable = false)
    private List<String> medicalServicesCode;

    @Column(nullable = false)
    private Boolean paid;

    private Boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Consultation() {
        this.deleted = false;
        this.paid = false;
    }
}
