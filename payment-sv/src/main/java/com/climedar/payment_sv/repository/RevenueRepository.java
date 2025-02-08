package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.Revenue;
import com.climedar.payment_sv.entity.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServicesType;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    List<Revenue> findByDateAndRevenueType(LocalDate date, RevenueType revenueType);

    Revenue findByDateAndRevenueTypeAndSpecialityNameAndMedicalServicesType(LocalDate date, RevenueType revenueType, String specialityName, ServicesType medicalServicesType);
}
