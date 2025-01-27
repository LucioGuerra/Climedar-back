package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Gender;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public static Specification<Doctor> nameLike(String name) {
        return (root, query, cb) -> name == null? cb.conjunction() : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Doctor> surnameLike(String surname) {
        return (root, query, cb) -> surname == null? cb.conjunction() : cb.like(root.get("surname"), "%" + surname + "%");
    }

    public static Specification<Doctor> dniLike(String dni) {
        return (root, query, cb) -> dni == null? cb.conjunction() : cb.like(root.get("dni"), "%" + dni + "%");
    }

    public static Specification<Doctor> genderEqual(Gender gender){
        return (root, query, cb) -> gender == null? cb.conjunction() : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Doctor> shiftIdEqual(Long shiftId){
        return (root, query, cb) -> shiftId == null? cb.conjunction() : cb.and(cb.isNotNull(root.get("shift")), cb.equal(root.get("shift").get("shiftId"), shiftId));
    }

    public static Specification<Doctor> specialtyIdEqual(Long specialtyId){
        return (root, query, cb) -> specialtyId == null? cb.conjunction() : cb.equal(root.get("specialty").get("specialtyId"), specialtyId);
    }

    public static Specification<Doctor> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }
}
