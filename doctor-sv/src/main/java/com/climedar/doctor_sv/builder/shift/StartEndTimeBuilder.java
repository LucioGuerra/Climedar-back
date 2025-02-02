package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

public class StartEndTimeBuilder {
    private final Shift shift;

    public StartEndTimeBuilder(){
        this.shift = new Shift();
    }

    public StartEndTimeBuilder date(LocalDate date){
        this.shift.setDate(date);
        return this;
    }

    public StartEndTimeBuilder time(LocalTime startTime, LocalTime endTime){
        this.shift.setStartTime(startTime);
        this.shift.setEndTime(endTime);
        return this;
    }

    public StartEndTimeBuilder place(String place){
        this.shift.setPlace(place);
        return this;
    }

    public StartEndTimeBuilder doctor(Doctor doctor){
        this.shift.setDoctor(doctor);
        return this;
    }

    public Shift build(){
        return this.shift;
    }
}
