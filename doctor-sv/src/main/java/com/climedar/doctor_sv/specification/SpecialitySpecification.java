package com.climedar.doctor_sv.specification;

import com.climedar.doctor_sv.entity.Speciality;
import org.springframework.data.jpa.domain.Specification;

public class SpecialitySpecification {

    public static Specification<Speciality> nameLike(String name) {
        return (root, query, cb) -> name == null? cb.conjunction() : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Speciality> descriptionLike(String description) {
        return (root, query, cb) -> description == null? cb.conjunction() : cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Speciality> codeLike(String code) {
        return (root, query, cb) -> code == null? cb.conjunction() : cb.like(root.get("code"), "%" + code + "%");
    }

    public static Specification<Speciality> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }
}
