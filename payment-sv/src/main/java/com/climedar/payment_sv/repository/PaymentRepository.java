package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    List<Payment> findByPaymentDateBetween(LocalDateTime paymentDateAfter, LocalDateTime paymentDateBefore);
}
