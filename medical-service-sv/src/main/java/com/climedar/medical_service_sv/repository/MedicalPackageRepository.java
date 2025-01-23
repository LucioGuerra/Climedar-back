package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalPackageRepository extends JpaRepository<MedicalPackageEntity, Long>, JpaSpecificationExecutor<MedicalPackageEntity> {
}
