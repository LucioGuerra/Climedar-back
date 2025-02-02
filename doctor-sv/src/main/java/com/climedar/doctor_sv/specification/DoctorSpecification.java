package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Gender;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public class DoctorSpecification {

    public static Specification<Doctor> shiftIdEqual(Long shiftId){
        return (root, query, cb) -> shiftId == null? cb.conjunction() : cb.and(cb.isNotNull(root.get("shift")), cb.equal(root.get("shift").get("shiftId"), shiftId));
    }

    public static Specification<Doctor> specialtyIdEqual(Long specialtyId){
        return (root, query, cb) -> specialtyId == null? cb.conjunction() : cb.equal(root.get("specialty").get("specialtyId"), specialtyId);
    }

    public static Specification<Doctor> personIdIn(Set<Long> personIds){
        return (root, query, cb) -> personIds == null? cb.conjunction() : root.get("personId").in(personIds);
    }

    public static Specification<Doctor> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }
}
