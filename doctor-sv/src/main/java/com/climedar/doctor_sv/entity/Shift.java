package com.climedar.doctor_sv.entity;

import com.climedar.doctor_sv.builder.shift.RecurringBuilder;
import com.climedar.doctor_sv.builder.shift.SinglePatientBuilder;
import com.climedar.doctor_sv.builder.shift.StarTimePatientBuilder;
import com.climedar.doctor_sv.builder.shift.StartEndTimeBuilder;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    private LocalTime endTime;

    private Integer patients;

    @Column(length = 50)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftState state;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Shift() {
        this.deleted = false;
        this.state = ShiftState.AVAILABLE;
    }

    public static StartEndTimeBuilder startEndTimeBuilder() {
        return new StartEndTimeBuilder();
    }

    public static StarTimePatientBuilder startTimePatientBuilder() {
        return new StarTimePatientBuilder();
    }

    public static SinglePatientBuilder singlePatientShiftBuilder() {
        return new SinglePatientBuilder();
    }

    public static RecurringBuilder recurringShiftBuilder() {
        return new RecurringBuilder();
    }
}
