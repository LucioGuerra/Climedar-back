package com.climedar.payment_sv.controller;

import com.climedar.payment_sv.dto.request.CreatePaymentDTO;
import com.climedar.payment_sv.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> createPayment(@RequestBody CreatePaymentDTO paymentDTO) {
        return paymentService.createPayment(paymentDTO);
    }
}
