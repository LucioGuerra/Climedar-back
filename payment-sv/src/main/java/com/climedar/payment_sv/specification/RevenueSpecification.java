package com.climedar.payment_sv.specification;

import com.climedar.payment_sv.entity.revenue.Revenue;
import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class RevenueSpecification {

    public static Specification<Revenue> byDate(String date, String startDate, String endDate) {
        return (root, query, cb) ->
        {
            Predicate predicate = cb.conjunction();
            if (date != null) {
                return cb.equal(root.get("date"), LocalDate.parse(date));
            }
            if(startDate != null){
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), LocalDate.parse(startDate)));
            }
            if(endDate != null){
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), LocalDate.parse(endDate)));
            }
            return predicate;
        };
    }


    public static Specification<Revenue> bySpeciality(String speciality) {
        return (root, query, cb) -> speciality == null? null : cb.equal(root.get("specialityName"), speciality);
    }

    public static Specification<Revenue> byMedicalService(ServiceType medicalService) {
        return (root, query, cb) -> medicalService == null? null : cb.equal(root.get("ServicesType"), medicalService);
    }


    public static Specification<Revenue> byType(RevenueType type) {
        return (root, query, cb) -> type == null? null : cb.equal(root.get("revenueType"), type);
    }
}
