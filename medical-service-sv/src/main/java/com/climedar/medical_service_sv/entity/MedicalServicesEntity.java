package com.climedar.medical_service_sv.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;

@Getter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class MedicalServicesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    public abstract Double getPrice();

    public abstract Duration getEstimatedDuration();
}
