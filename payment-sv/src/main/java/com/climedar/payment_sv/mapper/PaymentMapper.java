package com.climedar.payment_sv.mapper;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.model.PaymentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "consultation", expression = "java(this.getConsultation(payment))")
    PaymentModel toModel(Payment payment);


    default Consultation getConsultation(Payment payment) {
        return new Consultation(payment.getConsultationId());
    }
}
