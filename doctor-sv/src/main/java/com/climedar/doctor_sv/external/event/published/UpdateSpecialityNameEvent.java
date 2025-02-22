package com.climedar.doctor_sv.external.event.published;

import lombok.Data;

@Data
public class UpdateSpecialityNameEvent {
    private String newName;
    private String oldName;

    public UpdateSpecialityNameEvent(String newName, String oldName) {
        this.newName = newName;
        this.oldName = oldName;
    }
}
