package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {


    List<Payment> findByPaymentDateBetweenAndCanceled(LocalDateTime paymentDateAfter, LocalDateTime paymentDateBefore, Boolean canceled);

    Optional<Payment> findByIdAndCanceled(Long id, Boolean canceled);
}
