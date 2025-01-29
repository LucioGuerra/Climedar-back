package com.climedar.doctor_sv.model;

import com.climedar.doctor_sv.entity.ShiftState;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ShiftModel {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer patients;
    private String place;
    private ShiftState state;

    @AssertTrue(message = "Shift must have either end time or patients")
    private boolean isValidShift(){
        return (endTime == null) != (patients == null);
    }
}
