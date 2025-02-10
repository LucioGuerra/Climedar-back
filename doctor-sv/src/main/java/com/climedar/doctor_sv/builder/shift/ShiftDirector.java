package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ShiftDirector {

    public List<Shift> constructRecurringMultipleShifts(CreateShiftDTO shiftDTO, Doctor doctor) {
        List<Shift> shifts = new ArrayList<>();

        LocalDate endDate = LocalDate.parse(shiftDTO.getRecurringShift().getEndDate());
        LocalDate currentDate = LocalDate.parse(shiftDTO.getRecurringShift().getStartDate());
        Set<DayOfWeek> validDays = shiftDTO.getRecurringShift().getValidDays();

        while (currentDate.isBefore(endDate)) {
            if (validDays.contains(currentDate.getDayOfWeek())) {
                List<Shift> recurringShifts = Shift.multipleShiftBuilder()
                        .date(currentDate)
                        .startTime(LocalTime.parse(shiftDTO.getStartTime()))
                        .endTime(LocalTime.parse(shiftDTO.getEndTime()))
                        .timeOfShifts(Duration.parse(shiftDTO.getTimeOfShifts()))
                        .place(shiftDTO.getPlace())
                        .doctor(doctor)
                        .build();
                shifts.addAll(recurringShifts);
            }
            currentDate = currentDate.plusDays(1);
        }

        return shifts;
    }

    public List<Shift> constructMultipleShifts(CreateShiftDTO shiftDTO, Doctor doctor) {
        return Shift.multipleShiftBuilder()
                .date(LocalDate.parse(shiftDTO.getDate()))
                .startTime(LocalTime.parse(shiftDTO.getStartTime()))
                .endTime(LocalTime.parse(shiftDTO.getEndTime()))
                .timeOfShifts(Duration.parse(shiftDTO.getTimeOfShifts()))
                .place(shiftDTO.getPlace())
                .doctor(doctor)
                .build();
    }
}
