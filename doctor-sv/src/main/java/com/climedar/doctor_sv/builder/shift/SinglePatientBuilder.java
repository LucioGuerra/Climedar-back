package com.climedar.doctor_sv.builder.shift;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;

import java.time.LocalDate;
import java.time.LocalTime;

public class SinglePatientBuilder {

    private final Shift shift;

    public SinglePatientBuilder() {
        this.shift = new Shift();
        this.shift.setPatients(1);
    }

    public SinglePatientBuilder date(LocalDate date) {
        this.shift.setDate(date);
        return this;
    }

    public SinglePatientBuilder start(LocalTime startTime) {
        this.shift.setStartTime(startTime);
        return this;
    }

    public SinglePatientBuilder place(String place) {
        this.shift.setPlace(place);
        return this;
    }

    public SinglePatientBuilder doctor(Doctor doctor) {
        this.shift.setDoctor(doctor);
        return this;
    }

    public Shift build() {
        return this.shift;
    }

}
