package com.climedar.doctor_sv.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateShiftDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer patients;
    private String place;
    private Long doctorId;
    private RecurringShiftDTO recurringShift; //Puede ser null
    private ShiftCreateType shiftCreateType;
}
