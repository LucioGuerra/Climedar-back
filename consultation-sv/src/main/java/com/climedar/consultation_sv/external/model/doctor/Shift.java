package com.climedar.consultation_sv.external.model.doctor;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Shift {
    private Long id;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer patients;
    private ShiftState state;


    public Shift(Long id) {
        this.id = id;
    }
}
