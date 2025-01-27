package com.climedar.doctor_sv.model;

import com.climedar.doctor_sv.entity.ShiftState;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ShiftModel {
    private Long id;
    private LocalDate date;
    private LocalTime start;
    private Integer patients;
    private ShiftState state;
}
