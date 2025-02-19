package com.climedar.payment_sv.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentSpecificationDTO{
        private String date;
        private String fromDate;
        private String toDate;
        private BigDecimal amount;
        private BigDecimal fromAmount;
        private BigDecimal toAmount;
        private Boolean canceled;
        private Long consultationId;
        private Long patientId;
}
