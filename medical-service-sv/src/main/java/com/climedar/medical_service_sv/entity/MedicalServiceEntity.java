package com.climedar.medical_service_sv.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MedicalServiceEntity extends MedicalServicesEntity {


    @Column(unique = true, nullable = false, length = 17)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "service_type")
    private ServiceType serviceType;

    @Column(nullable = false, name = "speciality_id")
    private Long specialityId;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    public MedicalServiceEntity(){
        this.deleted = false;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }

    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
