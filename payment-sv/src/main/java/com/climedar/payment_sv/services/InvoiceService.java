package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Invoice;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.external.model.medical_services.MedicalService;
import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
import com.climedar.payment_sv.repository.ConsultationRepository;
import com.climedar.payment_sv.repository.InvoiceRepository;
import com.climedar.payment_sv.repository.MedicalServicesRepository;
import com.climedar.payment_sv.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final ExportService exportService;
    private final MedicalServicesRepository medicalServicesRepository;


    public byte[] generateInvoice(Payment payment) {
        /*Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        Patient patient = consultation.getPatient();
        List<MedicalServices> medicalServices = consultation.getMedicalServices();

        Invoice invoice = new Invoice();
        invoice.setPatientId(patient.getId());
        invoice.setTotalAmount(consultation.getFinalPrice());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setPayment(payment);

        List<Long> medicalServicesId = new ArrayList<>();
        for (MedicalServices medicalService : medicalServices) {
            medicalServicesId.add(medicalService.getId());
        }
        invoice.setMedicalServicesId(medicalServicesId);

        invoiceRepository.save(invoice);*/

        /*Map<String, Object> invoiceData = new HashMap<>();
        invoiceData.put("invoiceNumber", String.format("%10d", invoice.getId()));
        invoiceData.put("invoiceDate", invoice.getInvoiceDate());
        invoiceData.put("patientName", patient.getName());
        invoiceData.put("patientAddress", patient.getAddress().toString());
        invoiceData.put("patientPhone", patient.getPhone());
        invoiceData.put("patientEmail", patient.getEmail());
        invoiceData.put("finalPrice", consultation.getFinalPrice());
        invoiceData.put("services", medicalServices);
        invoiceData.put("paymentMethod", payment.getPaymentMethod());*/
        Map<String, Object> invoiceData = new HashMap<>();
        invoiceData.put("invoiceNumber", String.format("%10d", 123456));
        invoiceData.put("invoiceDate", LocalDate.now());
        invoiceData.put("patientName", "Juan Pérez");
        invoiceData.put("patientAddress", "Av. Siempre Viva 742");
        invoiceData.put("patientPhone", "+54 11 1234-5678");
        invoiceData.put("patientEmail", "juan.perez@example.com");
        invoiceData.put("finalPrice", new BigDecimal("3500.75"));
        invoiceData.put("services", List.of(
                new MedicalService("MS-THER-CAR-00000", new BigDecimal("600000000000"), "jajas"),

                new MedicalService( "MS-THER-CAR-00001", new BigDecimal("600000000000"), "Kamon")
        ));
        invoiceData.put("paymentMethod", "CREDIT_CARD");

        return exportService.getInvoicePDF(invoiceData);
    }


    /*public ResponseEntity<byte[]> getInvoiceByPayment(Long paymentId) {
        Invoice invoice = invoiceRepository.findByPatientId(paymentId).orElseThrow(() -> new EntityNotFoundException("Invoice not found for payment id: " + paymentId));
        Patient patient = patientRepository.getPatientById(invoice.getPatientId());
        MedicalServices medicalServices = medicalServicesRepository.getMedicalServiceById(invoice.getMedicalServicesId()).getMedicalServices();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("invoice_" + invoice.getPayment().getId() + ".pdf").build());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(exportService.getInvoicePDF(invoice, patient, medicalServices));
    }*/
}
