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

    public static Specification<Consultation> ByMedicalServicesCode(List<String> medicalServicesCodes) {
        return (root, query, cb) -> medicalServicesCodes == null? cb.conjunction(): root.get("medicalServicesCode").in(medicalServicesCodes);
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

    public static Specification<Consultation> ByDoctorId(Long doctorId) {
        return (root, query, cb) -> doctorId == null? cb.conjunction(): cb.equal(root.get("doctorId"), doctorId);
    }
}
