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
    private String date;
    private String fromDate;
    private String toDate;
    private String place;
    private String startTime;
    private String endTime;
    private String fromTime;
    private String toTime;

    @Positive(message = "Patients must be positive")
    private Integer patients;
    private ShiftState state;

    @AssertTrue(message = "From date must be before to date")
    public boolean isFromDateBeforeToDate() {
        if (fromDate == null || toDate == null) {
            return true;
        }
        return LocalDate.parse(fromDate).isBefore(LocalDate.parse(toDate));
    }

    @AssertTrue(message = "Start time must be before end time")
    public boolean isStartTimeBeforeEndTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return LocalTime.parse(startTime).isBefore(LocalTime.parse(endTime));
    }

    public ShiftSpecificationDTO() {
    }

    public ShiftSpecificationDTO(Long doctorId, String date) {
        this.doctorId = doctorId;
        this.date = date;
    }
}
