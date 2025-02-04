package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MultipleShiftBuilder {
    private final List<Shift> shifts;
    private Duration timeOfShifts;
    private LocalTime startTime;
    private LocalTime endTime;
    private Doctor doctor;
    private String place;
    private LocalDate date;


    public MultipleShiftBuilder(){
        this.shifts = new ArrayList<>();
    }

    public MultipleShiftBuilder timeOfShifts(Duration timeOfShifts){
        this.timeOfShifts = timeOfShifts;
        return this;
    }

    public MultipleShiftBuilder startTime(LocalTime startTime){
        this.startTime = startTime;
        return this;
    }

    public MultipleShiftBuilder endTime(LocalTime endTime){
        this.endTime = endTime;
        return this;
    }

    public MultipleShiftBuilder doctor(Doctor doctor){
        this.doctor = doctor;
        return this;
    }

    public MultipleShiftBuilder place(String place){
        this.place = place;
        return this;
    }

    public MultipleShiftBuilder date(LocalDate date){
        this.date = date;
        return this;
    }

    public List<Shift> build(){
        if (timeOfShifts == null || startTime == null || endTime == null){
            throw new IllegalArgumentException("Number of shifts, start time and end time must be set");
        }

        LocalTime time = startTime;
        while (time.isBefore(endTime.minus(timeOfShifts)) || time.equals(endTime.minus(timeOfShifts))){
            Shift shift = new Shift();
            shift.setDate(date);
            shift.setDoctor(doctor);
            shift.setPlace(place);
            shift.setStartTime(time);
            time = time.plus(timeOfShifts);
            shift.setEndTime(time);
            this.shifts.add(shift);
        }

        return this.shifts;
    }
}
