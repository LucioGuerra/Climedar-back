package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

public class StarTimePatientBuilder {

    private final Shift shift;

    public StarTimePatientBuilder(){
        this.shift = new Shift();
    }

    public StarTimePatientBuilder date(LocalDate date){
        this.shift.setDate(date);
        return this;
    }

    public StarTimePatientBuilder time(LocalTime startTime){
        this.shift.setStartTime(startTime);
        return this;
    }

    public StarTimePatientBuilder patients(Integer patients){
        this.shift.setPatients(patients);
        return this;
    }

    public StarTimePatientBuilder place(String place){
        this.shift.setPlace(place);
        return this;
    }

    public StarTimePatientBuilder doctor(Doctor doctor){
        this.shift.setDoctor(doctor);
        return this;
    }

    public Shift build(){
        return this.shift;
    }
}
