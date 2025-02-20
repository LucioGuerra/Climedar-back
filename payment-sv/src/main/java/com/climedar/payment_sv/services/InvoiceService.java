package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Invoice;
import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.external.model.Consultation;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
import com.climedar.payment_sv.external.model.medical_services.MedicalServicesWrapped;
import com.climedar.payment_sv.repository.ConsultationRepository;
import com.climedar.payment_sv.repository.InvoiceRepository;
import com.climedar.payment_sv.repository.MedicalServicesRepository;
import com.climedar.payment_sv.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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



    public byte[] createInvoice(Payment payment) {
        Consultation consultation = consultationRepository.getConsultation(payment.getConsultationId());
        Patient patient = consultation.getPatient();
        List<MedicalServices> medicalServices = consultation.getMedicalServicesModel();

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

        invoiceRepository.save(invoice);
        return exportService.getInvoicePDF(this.getInvoiceData(invoice, patient, medicalServices));
    }

    public ResponseEntity<byte[]> getInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new EntityNotFoundException("Invoice not found for id: " + invoiceId));
        return generateInvoice(invoice);
    }


    public ResponseEntity<byte[]> getInvoiceByPayment(Long paymentId) {
        Invoice invoice = invoiceRepository.findByPaymentId(paymentId).orElseThrow(() -> new EntityNotFoundException("Invoice not found for payment id: " + paymentId));
        return generateInvoice(invoice);
    }

    private ResponseEntity<byte[]> generateInvoice(Invoice invoice) {
        Patient patient = patientRepository.getPatientById(invoice.getPatientId());
        List<MedicalServices> medicalServices = medicalServicesRepository.getMedicalServicesByIds(invoice.getMedicalServicesId()).stream().map(MedicalServicesWrapped::getMedicalServices).toList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("invoice_" + invoice.getPayment().getId() + ".pdf").build());


        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(exportService.getInvoicePDF(this.getInvoiceData(invoice, patient, medicalServices)));
    }

    private Map<String, Object> getInvoiceData(Invoice invoice, Patient patient, List<MedicalServices> medicalServices) {
        Map<String, Object> invoiceData = new HashMap<>();
        invoiceData.put("invoiceNumber", String.format("%010d", invoice.getId()));
        invoiceData.put("invoiceDate", invoice.getInvoiceDate());
        invoiceData.put("patientName", patient.getName());
        invoiceData.put("patientAddress", patient.getAddress().toString());
        invoiceData.put("patientProvince", "La Plata");
        invoiceData.put("patientDni", patient.getDni());
        invoiceData.put("patientPhone", patient.getPhone());
        invoiceData.put("patientEmail", patient.getEmail());
        invoiceData.put("finalPrice", invoice.getTotalAmount().floatValue());
        invoiceData.put("services", medicalServices);
        invoiceData.put("paymentMethod", invoice.getPayment().getPaymentMethod().getDisplayName());
        return invoiceData;
    }
}
