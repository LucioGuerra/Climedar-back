package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftSpecification {

    public static Specification<Shift> byDate(LocalDate date, LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (date != null) {
                predicate = cb.and(predicate, cb.equal(root.get("date"), date));
            }
            if (fromDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), toDate));
            }

            return predicate;
        };
    }

    public static Specification<Shift> byStartTime(LocalTime startTime) {
        return (root, query, cb) -> startTime == null? cb.conjunction(): cb.equal(root.get("startTime"), startTime);
    }

    public static Specification<Shift> byEndTime(LocalTime endTime) {
        return (root, query, cb) -> endTime == null? cb.conjunction(): cb.equal(root.get("endTime"), endTime);
    }

    public static Specification<Shift> byPatients(Integer patients) {
        return (root, query, cb) -> patients == null? cb.conjunction(): cb.equal(root.get("patients"), patients);
    }

    public static Specification<Shift> byPlace(String place) {
        return (root, query, cb) -> place == null? cb.conjunction(): cb.equal(root.get("place"), place);
    }

    public static Specification<Shift> byState(ShiftState state) {
        return (root, query, cb) -> state == null? cb.conjunction(): cb.equal(root.get("state"), state);
    }

    public static Specification<Shift> byDoctorId(Long doctorId) {
        return (root, query, cb) -> doctorId == null? cb.conjunction(): cb.equal(root.get("doctor").get("id"), doctorId);
    }

    public static Specification<Shift> byDeleted(Boolean deleted) {
        return (root, query, cb) -> deleted == null? cb.conjunction(): cb.equal(root.get("deleted"), deleted);
    }
}
