package com.climedar.payment_sv.services;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.event.internal.PaymentEvent;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.mapper.PaymentMapper;
import com.climedar.payment_sv.model.PaymentModel;
import com.climedar.payment_sv.repository.ConsultationRepository;
import com.climedar.payment_sv.repository.PaymentRepository;
import com.climedar.payment_sv.specification.PaymentSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final PaymentMapper paymentMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ConsultationRepository consultationRepository;

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

    public Page<PaymentModel> getAllPayments(Pageable pageable, LocalDate date, LocalDate fromDate, LocalDate toDate,
                                        Double amount, Double fromAmount, Double toAmount,
                                        Boolean canceled, Long patientId, Long consultationId) {

        Specification<Payment> spec = Specification.where(PaymentSpecification.paymentByCanceled(canceled))
                .and(PaymentSpecification.paymentByDate(date, fromDate, toDate))
                .and(PaymentSpecification.paymentByAmount(amount, fromAmount, toAmount))
                .and(PaymentSpecification.paymentByPatientId(patientId))
                .and(PaymentSpecification.paymentByConsultationId(consultationId));

        Page<Payment> payments = paymentRepository.findAll(spec, pageable);

        List<Long> consultationIds = payments.map(Payment::getConsultationId).getContent();
        List<Consultation> consultations = consultationRepository.findAllById(consultationIds);

       Map<Long, Patient> consultationPatientMap = consultations.stream()
                .collect(Collectors.toMap(Consultation::getId, Consultation::getPatient));

        return payments.map(payment -> {
            PaymentModel paymentModel = paymentMapper.toModel(payment);
            paymentModel.setPatient(consultationPatientMap.get(payment.getConsultationId()));
            return paymentModel;
        });
    }



    public List<Payment> getPaymentsByDate(LocalDate date) {
        return paymentRepository.findByPaymentDateBetweenAndCanceled(date.atStartOfDay(), date.atTime(23, 59, 59),
                false);
    }
}
