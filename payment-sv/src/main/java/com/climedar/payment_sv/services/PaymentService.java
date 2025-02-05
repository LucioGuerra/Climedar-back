package com.climedar.payment_sv.services;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.event.internal.PaymentEvent;
import com.climedar.payment_sv.mapper.PaymentMapper;
import com.climedar.payment_sv.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final PaymentMapper paymentMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ResponseEntity<byte[]> createPayment(CreatePaymentDTO paymentDTO) {
        Payment payment = paymentMapper.toEntity(paymentDTO);
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentEvent(payment.getAmount(), payment.getPaymentDate()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("invoice_"+payment.getId()+".pdf").build());


        //todo: retornar tambien el recibo
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(invoiceService.generateInvoice(payment));
    }

    public ResponseEntity<Void> cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findByIdAndCanceled(paymentId, false)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));
        payment.setCanceled(true);
        eventPublisher.publishEvent(new PaymentEvent(payment.getAmount().negate(), payment.getPaymentDate()));
        return ResponseEntity.noContent().build();
    }

    public List<Payment> getPaymentsByDate(LocalDate date) {
        return paymentRepository.findByPaymentDateBetween(date.atStartOfDay(), date.atTime(23, 59, 59));
    }
}
