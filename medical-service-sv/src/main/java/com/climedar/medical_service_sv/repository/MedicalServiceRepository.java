package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalServiceEntity, Long>, JpaSpecificationExecutor<MedicalServiceEntity> {
    @Query("SELECT m FROM MedicalServiceEntity m WHERE m.id IN :ids AND m.deleted = false")
    Set<MedicalServiceEntity> findByIdsAndNotDeleted(List<Long> ids);

    @Query("SELECT m FROM MedicalServiceEntity m WHERE m.id = :id AND m.deleted = false")
    Optional<MedicalServiceEntity> findByIdAndNotDeleted(Long id);
}
