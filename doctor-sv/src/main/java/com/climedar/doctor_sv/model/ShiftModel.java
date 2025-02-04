package com.climedar.doctor_sv.model;

import com.climedar.doctor_sv.entity.ShiftState;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ShiftModel {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration timeOfShifts;
    private String place;
    private ShiftState state;
    private DoctorModel doctor;

    @AssertTrue(message = "Start time must be before end time")
    public boolean isStartTimeBeforeEndTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return startTime.isBefore(endTime);
    }
}
