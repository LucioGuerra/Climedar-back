package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.entity.MedicalServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalServicesRepository extends JpaRepository<MedicalServices, Long> {

    @Query(value = "SELECT id, 'MedicalServiceEntity' as tipo FROM medical_service_entity WHERE id = :id " +
            "UNION " +
            "SELECT id, 'MedicalPackageEntity' as tipo FROM medical_package_entity WHERE id = :id", nativeQuery = true)
    MedicalServices findMedicalServicesById(Long id);
}
