package com.climedar.payment_sv.external.event.received;

import lombok.Data;

@Data
public class UpdateSpecialityNameEvent {
    private String newName;
    private String oldName;
}
