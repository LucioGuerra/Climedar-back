package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByPatientId(Long patientId);

    @Query("SELECT i FROM Invoice i WHERE i.payment.id = :paymentId")
    Optional<Invoice> findByPaymentId(Long paymentId);
}
