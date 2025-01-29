package com.climedar.doctor_sv.dto.request.specification;

import com.climedar.doctor_sv.entity.ShiftState;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShiftSpecificationDTO {
    private Long doctorId;
    private LocalDate date;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String place;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer patients;
    private ShiftState state;
}
