package com.climedar.consultation_sv.specification;

import com.climedar.consultation_sv.entity.Consultation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.List;

public class ConsultationSpecification {

    public static Specification<Consultation> ByPatientId(Long patientId) {
        return (root, query, cb) -> patientId == null? cb.conjunction(): cb.equal(root.get("patientId"), patientId);
    }

    public static Specification<Consultation> ByShiftId(List<Long> shiftId) {
        return (root, query, cb) -> shiftId.isEmpty()? cb.conjunction(): root.get("shiftId").in(shiftId);
    }

    public static Specification<Consultation> ByMedicalServicesId(Long medicalServicesId) {
        return (root, query, cb) -> medicalServicesId == null? cb.conjunction(): cb.equal(root.get("medicalServicesId"), medicalServicesId);
    }

    public static Specification<Consultation> ByTime(LocalTime startTime, LocalTime fromTime, LocalTime toTime) {
        return (root, query, cb) -> {
            if (startTime != null) {
                return cb.equal(root.get("startTime"), startTime);
            }else if (fromTime == null && toTime == null) {
                return cb.conjunction();
            } else if (fromTime == null) {
                return cb.lessThanOrEqualTo(root.get("startTime"), toTime);
            } else if (toTime == null) {
                return cb.greaterThanOrEqualTo(root.get("startTime"), fromTime);
            } else {
                return cb.between(root.get("startTime"), fromTime, toTime);
            }
        };
    }

    public static Specification<Consultation> likeDescription(String description) {
        return (root, query, cb) -> description == null? cb.conjunction(): cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Consultation> likeObservation(String observation) {
        return (root, query, cb) -> observation == null? cb.conjunction(): cb.like(root.get("observation"), "%" + observation + "%");
    }

    public static Specification<Consultation> byDeleted(Boolean deleted) {
        return (root, query, cb) -> deleted == null? cb.conjunction(): cb.equal(root.get("deleted"), deleted);
    }
}
