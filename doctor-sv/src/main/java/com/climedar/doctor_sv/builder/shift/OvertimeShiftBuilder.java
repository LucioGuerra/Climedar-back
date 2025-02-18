package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class OvertimeShiftBuilder {
    private Shift shift;
    private Doctor doctor;
    private Duration timeOfShifts;

    public OvertimeShiftBuilder() {
        this.shift = new Shift();
    }

    public OvertimeShiftBuilder doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public OvertimeShiftBuilder duration(String duration) {
        this.timeOfShifts = Duration.parse(duration);
        return this;
    }

    public Shift build(){
        this.shift.setDate(LocalDate.now());
        this.shift.setStartTime(LocalTime.now());
        this.shift.setEndTime(LocalTime.now().plus(timeOfShifts));
        this.shift.setDoctor(doctor);

        return this.shift;
    }


}
