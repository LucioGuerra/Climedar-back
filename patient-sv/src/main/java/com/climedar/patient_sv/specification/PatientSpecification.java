package com.climedar.patient_sv.specification;

import com.climedar.patient_sv.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class PatientSpecification {

    public static Specification<Patient> medicalSecureIdEqual(Long medicalSecureId){
        return (root, query, cb) -> medicalSecureId == null? cb.conjunction() :
                cb.equal(root.get("medicalSecure").get("id"), medicalSecureId);
    }

    public static Specification<Patient> personIdIn(Set<Long> personIds){
        return (root, query, cb) -> personIds == null? cb.conjunction() : root.get("personId").in(personIds);
    }

    public static Specification<Patient> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }

}
