package com.climedar.payment_sv.controller;

import com.climedar.payment_sv.services.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoice(id);
    }

    @GetMapping(path = "/payment/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvoiceByPayment(@PathVariable Long id) {
        return invoiceService.getInvoiceByPayment(id);
    }

}
