package com.climedar.doctor_sv.dto.request;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Data
public class RecurringShiftDTO {
    private LocalDate startDate;
    private LocalDate EndDate;
    private Set<DayOfWeek> validDays;
}
