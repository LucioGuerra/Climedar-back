package com.climedar.payment_sv.services;



import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@AllArgsConstructor
@Service
public class ExportService {

    private final TemplateEngine templateEngine;


    @SneakyThrows
    public byte[] getInvoicePDF(Map<String, Object> invoiceData) {
        Context context = new Context();
        context.setVariables(invoiceData);

        String html = templateEngine.process("invoice/invoice_template", context);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();


        String baseUrl = new ClassPathResource("/static/").getURL().toExternalForm();
        renderer.setDocumentFromString(html, baseUrl);
        renderer.layout();
        renderer.createPDF(baos, false);
        renderer.finishPDF();

        return baos.toByteArray();
    }

   /* @SneakyThrows
    public byte[] getReceiptPDF(Payment payment, Patient patient, List<MedicalServices> medicalServices) {
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
    }*/
}
