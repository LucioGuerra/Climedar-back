package com.climedar.payment_sv.services;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.mapper.PaymentMapper;
import com.climedar.payment_sv.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final PaymentMapper paymentMapper;

    public ResponseEntity<byte[]> createPayment(CreatePaymentDTO paymentDTO) {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        paymentRepository.save(payment);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("invoice_"+payment.getId()+".pdf").build());

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(invoiceService.generateInvoice(payment));
    }
}
