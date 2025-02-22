package com.climedar.payment_sv.services;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.dto.request.PaymentSpecificationDTO;
import com.climedar.payment_sv.entity.Invoice;
import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.event.PaymentEvent;
import com.climedar.payment_sv.external.event.published.ConfirmedPayEvent;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.external.model.medical_services.MedicalPackage;
import com.climedar.payment_sv.external.model.medical_services.MedicalService;
import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
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
    private final ExportService exportService;
    private final ConsultationRepository consultationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ResponseEntity<byte[]> createPayment(CreatePaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setConsultationId(paymentDTO.consultationId());
        Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        payment.setAmount(consultation.getFinalPrice());
        payment.setInvoice(invoiceService.createInvoice(payment));
        payment.setPaymentMethod(paymentDTO.paymentMethod());

        paymentRepository.save(payment);
        kafkaTemplate.send("confirmed-pay", new ConfirmedPayEvent(payment.getConsultationId()));

        for (MedicalServices medicalService : consultation.getMedicalServicesModel()) {
            if (medicalService.getClass() == MedicalService.class) {
                eventPublisher.publishEvent(new PaymentEvent(medicalService.getPrice(), payment.getPaymentDate(),
                        ((MedicalService) medicalService).getServiceType(), ((MedicalService) medicalService).getSpeciality().getName()));
            }else {
                for (MedicalService medicalService1 : ((MedicalPackage) medicalService).getMedicalServices()) {
                    eventPublisher.publishEvent(new PaymentEvent(medicalService1.getPrice(), payment.getPaymentDate(),
                            medicalService1.getServiceType(), medicalService1.getSpeciality().getName()));
                }
            }
        }

        //todo: retornar tambien el recibo
        return invoiceService.getInvoiceByPayment(payment.getId());
    }

    public Boolean cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findByIdAndCanceled(paymentId, false)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));
        Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        payment.setCanceled(true);

        for (MedicalServices medicalService : consultation.getMedicalServicesModel()) {
            if (medicalService.getClass() == MedicalService.class) {
                eventPublisher.publishEvent(new PaymentEvent(medicalService.getPrice().negate(), payment.getPaymentDate(),
                        ((MedicalService) medicalService).getServiceType(), ((MedicalService) medicalService).getSpeciality().getName()));
            }else {
                for (MedicalService medicalService1 : ((MedicalPackage) medicalService).getMedicalServices()) {
                    eventPublisher.publishEvent(new PaymentEvent(medicalService1.getPrice().negate(), payment.getPaymentDate(),
                            medicalService1.getServiceType(), medicalService1.getSpeciality().getName()));
                }
            }
        }
        return true;
    }

    public Page<PaymentModel> getAllPayments(Pageable pageable, PaymentSpecificationDTO specificationDTO) {

        Specification<Payment> spec =
                Specification.where(PaymentSpecification.paymentByCanceled(specificationDTO.getCanceled()))
                .and(PaymentSpecification.paymentByDate(specificationDTO.getDate(), specificationDTO.getFromDate(), specificationDTO.getToDate()))
                .and(PaymentSpecification.paymentByAmount(specificationDTO.getAmount(),
                                specificationDTO.getFromAmount(),
                                specificationDTO.getToAmount())
                .and(PaymentSpecification.paymentByPatientId(specificationDTO.getPatientId()))
                .and(PaymentSpecification.paymentByConsultationId(specificationDTO.getConsultationId())));

        Page<Payment> payments = paymentRepository.findAll(spec, pageable);

        if (payments.isEmpty()) {
            return Page.empty();
        }

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

    public PaymentModel getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toModel(payment);
    }

    public ResponseEntity<byte[]> getReceiptByPayment(Long paymentId) {
        Payment payment = paymentRepository.findByIdAndCanceled(paymentId, false)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("receipt_"+payment.getId()+".pdf").build());

        Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        Patient patient = consultation.getPatient();
        List<MedicalServices> medicalServices = consultation.getMedicalServicesModel();
        Invoice invoice = payment.getInvoice();

        Map<String, Object> receiptData = new HashMap<>();
        receiptData.put("receiptNumber", String.format("%010d", payment.getId()));
        receiptData.put("invoiceNumber", String.format("%010d", invoice.getId()));
        receiptData.put("paymentDate", payment.getPaymentDate().toLocalDate());
        receiptData.put("patientName", patient.getName());
        receiptData.put("patientAddress", patient.getAddress().toString());
        receiptData.put("patientProvince", "La Plata");
        receiptData.put("patientDni", patient.getDni());
        receiptData.put("patientPhone", patient.getPhone());
        receiptData.put("patientEmail", patient.getEmail());
        receiptData.put("finalPrice", consultation.getFinalPrice());
        receiptData.put("services", medicalServices);
        receiptData.put("paymentMethod", payment.getPaymentMethod().getDisplayName());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(exportService.getReceiptPDF(receiptData));
    }

    public ResponseEntity<byte[]> getInvoiceByConsultation(Long consultationId) {
        Payment payment = paymentRepository.findByConsultationId(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for consultation id: " + consultationId));

        return invoiceService.getInvoiceByPayment(payment.getId());
    }


    public List<Payment> getPaymentsByDate(LocalDate date) {
        return paymentRepository.findByPaymentDateBetweenAndCanceled(date.atStartOfDay(), date.atTime(23, 59, 59),
                false);
    }

}
