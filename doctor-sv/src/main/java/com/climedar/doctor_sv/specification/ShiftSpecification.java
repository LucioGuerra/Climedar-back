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

    public static Specification<Shift> byDate(String date, String fromDate, String toDate) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (date != null && !date.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("date"), LocalDate.parse(date)));
            }
            if (fromDate != null && !fromDate.isEmpty()) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("date"), LocalDate.parse(fromDate)));
            }
            if (toDate != null && !toDate.isEmpty()) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("date"), LocalDate.parse(toDate)));
            }

            return predicate;
        };
    }

    public static Specification<Shift> byTime(String fromTime, String toTime) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (fromTime != null && !fromTime.isEmpty()) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startTime"),
                        LocalTime.parse(fromTime)));
            }
            if (toTime != null && !toTime.isEmpty()) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("endTime"), LocalTime.parse(toTime)));
            }

            return predicate;
        };
    }

    public static Specification<Shift> byStartTime(String startTime) {
        return (root, query, cb) -> startTime == null || startTime.isEmpty() ? cb.conjunction(): cb.equal(root.get(
                "startTime"),
                LocalTime.parse(startTime));
    }

    public static Specification<Shift> byEndTime(String endTime) {
        return (root, query, cb) -> endTime == null || endTime.isEmpty() ? cb.conjunction(): cb.equal(root.get("endTime"),
                LocalTime.parse(endTime));
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
