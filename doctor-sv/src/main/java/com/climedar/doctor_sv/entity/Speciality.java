package com.climedar.doctor_sv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false, unique = true)
    private String code;

    private Boolean deleted;

    public Speciality() {
        this.deleted = false;
    }
}
