package com.climedar.medical_service_sv.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class MedicalPackageEntity extends MedicalServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 28)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private Set<MedicalServiceEntity> services;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    public MedicalPackageEntity(){
        this.services = new HashSet<>();
        this.deleted = false;
    }

    @Override
    public Double getPrice() {
        double sumPrice = this.services.stream().mapToDouble(MedicalServiceEntity::getPrice).sum();
        return sumPrice * 0.85;
    }

    public void addService(MedicalServiceEntity service){
        services.add(service);
    }

    public void removeService(MedicalServiceEntity service){
        services.remove(service);
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
