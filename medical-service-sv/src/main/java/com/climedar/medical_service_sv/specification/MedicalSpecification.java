package com.climedar.medical_service_sv.specification;

import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.entity.ServiceType;
import org.springframework.data.jpa.domain.Specification;

public class MedicalSpecification {
    public static Specification<MedicalServiceEntity> codeContains(String code){
        return (root, query, cb) -> code == null? null: cb.like(root.get("code"), "%" + code + "%");
    }

    public static Specification<MedicalServiceEntity> nameContains(String name){
        return (root, query, cb) -> name == null? null: cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<MedicalServiceEntity> descriptionContains(String description){
        return (root, query, cb) -> description == null? null: cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<MedicalServiceEntity> serviceTypeEqual(ServiceType serviceType){
        return (root, query, cb) -> serviceType == null? null: cb.equal(root.get("serviceType"), serviceType);
    }

    public static Specification<MedicalServiceEntity> specialityIdEqual(Long specialityId){
        return (root, query, cb) -> specialityId == null? null: cb.equal(root.get("specialityId"), specialityId);
    }

    public static Specification<MedicalServiceEntity> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? null: cb.equal(root.get("deleted"), deleted);
    }
}
