package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class DoctorSpecification {

    public static Specification<Doctor> shiftIdEqual(Long shiftId){
        return (root, query, cb) -> shiftId == null? cb.conjunction() : cb.and(cb.isNotNull(root.get("shift")), cb.equal(root.get("shift").get("shiftId"), shiftId));
    }

    public static Specification<Doctor> specialityIdEqual(Long specialityId){
        return (root, query, cb) -> specialityId == null? cb.conjunction() :
                cb.equal(root.get("speciality").get("id"), specialityId);
    }

    public static Specification<Doctor> personIdIn(Set<Long> personIds){
        return (root, query, cb) -> personIds == null? cb.conjunction() : root.get("personId").in(personIds);
    }

    public static Specification<Doctor> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }

}
