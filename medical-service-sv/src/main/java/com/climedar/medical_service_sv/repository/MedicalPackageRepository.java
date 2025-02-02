package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalPackageRepository extends JpaRepository<MedicalPackageEntity, Long>, JpaSpecificationExecutor<MedicalPackageEntity> {

    @Query("SELECT p FROM MedicalPackageEntity p JOIN p.services s WHERE s.id = :id")
    List<MedicalPackageEntity> findByServiceId(Long id);

    @Query("SELECT p FROM MedicalPackageEntity p WHERE p.id = :id AND p.deleted = false")
    Optional<MedicalPackageEntity> findByIdAndNotDeleted(Long id);
}
