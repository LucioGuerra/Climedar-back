package com.climedar.payment_sv.specification;

import com.climedar.payment_sv.entity.Revenue;
import com.climedar.payment_sv.entity.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServicesType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class RevenueSpecification {

    public static Specification<Revenue> byDate(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) ->
        {
            Predicate predicate = cb.conjunction();
            if (date != null) {
                return cb.equal(root.get("date"), date);
            }
            if(startDate != null){
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if(endDate != null){
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), endDate));
            }
            return predicate;
        };
    }


    public static Specification<Revenue> bySpeciality(String speciality) {
        return (root, query, cb) -> speciality == null? null : cb.equal(root.get("specialityName"), speciality);
    }

    public static Specification<Revenue> byMedicalService(ServicesType medicalService) {
        return (root, query, cb) -> medicalService == null? null : cb.equal(root.get("medicalServicesType"), medicalService);
    }


    public static Specification<Revenue> byType(RevenueType type) {
        return (root, query, cb) -> type == null? null : cb.equal(root.get("revenueType"), type);
    }
}
