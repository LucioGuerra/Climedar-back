package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftSpecification {

    public static Specification<Shift> byDate(LocalDate date) {
        return (root, query, cb) -> date == null? cb.conjunction(): cb.equal(root.get("date"), date);
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
}
