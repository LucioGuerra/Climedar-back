package com.climedar.doctor_sv.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShiftDTO {
    @NotNull(message = "Date is required")
    //@FutureOrPresent(message = "Date must be in the future or present")
    private String date;

    private String startTime;

    private String endTime;

    @NotNull(message = "Time of shifts is required")
    private String timeOfShifts;

    private String place;

    @NotNull(message = "Doctor id is required")
    private Long doctorId;

    private RecurringShiftDTO recurringShift;

    private ShiftBuilder shiftBuilder;


    public CreateShiftDTO(Long doctorId, Duration timeOfShifts) {
        this.doctorId = doctorId;
        this.timeOfShifts = timeOfShifts.toString();
    }
}
