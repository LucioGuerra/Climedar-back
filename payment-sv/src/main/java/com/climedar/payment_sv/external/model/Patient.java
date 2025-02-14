package com.climedar.payment_sv.external.model;

import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

@Data
public class Patient {
    private Long id;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phone;
    private Address address;

}
