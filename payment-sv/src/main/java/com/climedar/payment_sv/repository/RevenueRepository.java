package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.revenue.Revenue;
import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long>, JpaSpecificationExecutor<Revenue> {

    List<Revenue> findByDateAndRevenueType(LocalDate date, RevenueType revenueType);

    Revenue findByDateAndRevenueTypeAndSpecialityNameAndMedicalServiceType(LocalDate date, RevenueType revenueType, String specialityName, ServiceType medicalServiceType);
}
