package com.climedar.payment_sv.model;

import com.climedar.payment_sv.entity.payment.PaymentMethod;
import com.climedar.payment_sv.external.model.Patient;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentModel {
    private Long id;
    private BigDecimal amount;
    private String paymentDate;
    private Boolean canceled;
    private Patient patient;
    private PaymentMethod paymentMethod;
}
