package com.climedar.doctor_sv.dto.request.specification;

import com.climedar.doctor_sv.entity.ShiftState;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "Patients must be positive")
    private Integer patients;
    private ShiftState state;

    @AssertTrue(message = "From date must be before to date")
    public boolean isFromDateBeforeToDate() {
        if (fromDate == null || toDate == null) {
            return true;
        }
        return fromDate.isBefore(toDate);
    }

    @AssertTrue(message = "Start time must be before end time")
    public boolean isStartTimeBeforeEndTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return startTime.isBefore(endTime);
    }

    public ShiftSpecificationDTO() {
    }

    public ShiftSpecificationDTO(Long doctorId, LocalDate date) {
        this.doctorId = doctorId;
        this.date = date;
    }
}
