package com.climedar.patient_sv.specification;

import com.climedar.patient_sv.entity.MedicalSecure;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class MedicalSecureSpecification {

    public static Specification<MedicalSecure> nameLike(String name){
        return (root, query, cb) -> name == null? cb.conjunction() : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<MedicalSecure> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }

}
