package com.climedar.person_sv.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(length = 5)
    private String floor;

    @Column(length = 5)
    private String apartment;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
