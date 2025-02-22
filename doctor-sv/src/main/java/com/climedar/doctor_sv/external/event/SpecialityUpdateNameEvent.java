package com.climedar.doctor_sv.external.event;

import lombok.Data;

@Data
public class SpecialityUpdateNameEvent {
    private String name;

    public SpecialityUpdateNameEvent(String name) {
        this.name = name;
    }
}
