package com.climedar.payment_sv.mapper;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.model.PaymentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(Payment payment);
}
