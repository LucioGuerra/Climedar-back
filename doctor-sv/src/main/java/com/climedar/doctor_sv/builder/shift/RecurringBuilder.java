package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecurringBuilder {

    private final List<Shift> shifts;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<DayOfWeek> validDays;
    private Shift shiftTemplate;

    public RecurringBuilder() {
        this.shifts = new ArrayList<>();
    }

    public RecurringBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public RecurringBuilder endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public RecurringBuilder validDays(Set<DayOfWeek> validDays) {
        this.validDays = validDays;
        return this;
    }

    public RecurringBuilder shiftTemplate(Shift shiftTemplate) {
        this.shiftTemplate = shiftTemplate;
        return this;
    }

    public List<Shift> build() {

        if (startDate == null || endDate == null || validDays == null || shiftTemplate == null) {
            throw new IllegalStateException("Cannot build recurring shifts without all required fields");
        }

        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate)) {
            if (validDays.contains(currentDate.getDayOfWeek())) {
                Shift newShift = new Shift();
                newShift.setDate(currentDate);
                newShift.setStartTime(shiftTemplate.getStartTime());
                newShift.setEndTime(shiftTemplate.getEndTime());
                newShift.setPatients(shiftTemplate.getPatients());
                newShift.setPlace(shiftTemplate.getPlace());
                newShift.setDoctor(shiftTemplate.getDoctor());
                shifts.add(newShift);
            }
            currentDate = currentDate.plusDays(1);
        }
        return shifts;
    }
}
