package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.entity.revenue.Revenue;
import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long>, JpaSpecificationExecutor<Revenue> {

    List<Revenue> findByDateAndRevenueType(LocalDate date, RevenueType revenueType);

    Revenue findByDateAndRevenueTypeAndSpecialityNameAndMedicalServiceType(LocalDate date, RevenueType revenueType, String specialityName, ServiceType medicalServiceType);

    @Query("SELECT r.date, SUM(r.amount) " +
            "FROM Revenue r " +
            "WHERE r.date BETWEEN :from AND :to AND r.revenueType = :revenueType " +
            "GROUP BY r.date " +
            "ORDER BY r.date ASC")
    List<Object[]> findByDateBetweenAndRevenueType(@Param("from") LocalDate fromDate,
                                                   @Param("to") LocalDate toDate,
                                                   @Param("revenueType") RevenueType revenueType);

    @Modifying
    @Query("UPDATE Revenue r SET r.specialityName = :newName WHERE r.specialityName = :oldName")
    void updateSpecialityName(@Param("newName") String newName, @Param("oldName") String oldName);
}
