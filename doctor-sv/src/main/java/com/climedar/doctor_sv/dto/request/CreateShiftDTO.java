package com.climedar.doctor_sv.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShiftDTO {
    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be in the future or present")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Patients is required")
    @Positive(message = "Patients must be positive")
    @Min(value = 1, message = "Patients must be at least 1")
    private Integer patients;

    private String place;

    @NotNull(message = "Doctor id is required")
    private Long doctorId;


    private RecurringShiftDTO recurringShift;

    @NotNull(message = "Shift create type is required")
    private ShiftCreateType shiftCreateType;
}
