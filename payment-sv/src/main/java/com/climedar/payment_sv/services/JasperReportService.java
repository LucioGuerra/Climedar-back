package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Invoice;
import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.external.model.Patient;
import com.climedar.payment_sv.external.model.medical_services.MedicalService;
import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {


    @SneakyThrows
    public byte[] getInvoicePDF(Invoice invoice, Patient patient, MedicalServices medicalServices) {
        InputStream inputStream = getClass().getResourceAsStream("/jasper_report/invoice_template.jasper");


        Map<String, Object> parameters = new HashMap<>();

        parameters.put("invoice_id", invoice.getId());
        parameters.put("patient_name", patient.getName());
        parameters.put("patient_address", patient.getAddress());
        parameters.put("patient_phone", patient.getPhone());
        parameters.put("patient_email", patient.getEmail());
        parameters.put("patient_surname", patient.getSurname());
        parameters.put("patient_dni", patient.getDni());
        parameters.put("total_amount", invoice.getTotalAmount());
        parameters.put("invoice_date", invoice.getInvoiceDate());
        parameters.put("services_names", medicalServices.getName());
        parameters.put("services_prices", medicalServices.getPrice());
        parameters.put("services_codes", medicalServices.getCode());

         JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource());
         return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @SneakyThrows
    public byte[] getReceiptPDF(Payment payment, Patient patient, MedicalServices medicalServices) {
        InputStream inputStream = getClass().getResourceAsStream("/jasper_report/receipt_template.jasper");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("receipt_id", payment.getId());
        parameters.put("patient_name", patient.getName());
        parameters.put("patient_address", patient.getAddress());
        parameters.put("patient_phone", patient.getPhone());
        parameters.put("patient_email", patient.getEmail());
        parameters.put("patient_surname", patient.getSurname());
        parameters.put("patient_dni", patient.getDni());
        parameters.put("total_amount", payment.getAmount());
        parameters.put("invoice_date", payment.getPaymentDate());
        parameters.put("services_names", medicalServices.getName());
        parameters.put("services_prices", medicalServices.getPrice());
        parameters.put("services_codes", medicalServices.getCode());

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
