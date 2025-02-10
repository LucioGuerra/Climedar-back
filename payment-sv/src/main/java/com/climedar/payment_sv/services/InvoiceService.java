package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Invoice;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
import com.climedar.payment_sv.repository.ConsultationRepository;
import com.climedar.payment_sv.repository.InvoiceRepository;
import com.climedar.payment_sv.repository.MedicalServicesRepository;
import com.climedar.payment_sv.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final JasperReportService jasperReportService;
    private final MedicalServicesRepository medicalServicesRepository;


    public byte[] generateInvoice(Payment payment) {
        Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        //Patient patient = consultation.getPatient();
        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("John");
        patient.setSurname("Doe");
        patient.setAddress("123 Main St");
        patient.setDni("12345678");
        patient.setPhone("123456789");
        patient.setEmail("lala@lala.com");
        MedicalServices medicalServices = consultation.getMedicalServices();

        Invoice invoice = new Invoice();
        invoice.setPatientId(patient.getId());
        invoice.setTotalAmount(consultation.getFinalPrice());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setMedicalServicesId(medicalServices.getId());
        invoice.setPayment(payment);

        invoiceRepository.save(invoice);
        return jasperReportService.getInvoicePDF(invoice, patient, medicalServices);
    }


    public ResponseEntity<byte[]> getInvoiceByPayment(Long paymentId) {
        Invoice invoice = invoiceRepository.findByPatientId(paymentId).orElseThrow(() -> new EntityNotFoundException("Invoice not found for payment id: " + paymentId));
        Patient patient = patientRepository.getPatientById(invoice.getPatientId());
        MedicalServices medicalServices = medicalServicesRepository.getMedicalServiceById(invoice.getMedicalServicesId()).getMedicalServices();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("invoice_" + invoice.getPayment().getId() + ".pdf").build());

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(jasperReportService.getInvoicePDF(invoice, patient, medicalServices));
    }
}
