package com.climedar.doctor_sv.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double salary;

    @Column(name = "person_id", nullable = false, unique = true)
    private Long personId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Shift> shifts;

    @ManyToOne(cascade = CascadeType.ALL)
    private Speciality speciality;

    @Column(nullable = false)
    private Boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Doctor() {
        this.deleted = false;
    }
}
