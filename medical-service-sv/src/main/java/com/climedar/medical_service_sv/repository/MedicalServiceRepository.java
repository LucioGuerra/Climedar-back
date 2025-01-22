package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalServiceEntity, Long> {
}
