package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.Revenue;
import com.climedar.payment_sv.entity.RevenueType;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    Optional<Revenue> findByDateAndRevenueType(LocalDate date, RevenueType revenueType);
}
