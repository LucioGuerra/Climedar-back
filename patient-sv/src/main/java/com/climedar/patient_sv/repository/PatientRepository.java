package com.climedar.patient_sv.repository;

import com.climedar.patient_sv.entity.Patient;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Patient d WHERE d.personId = :personId AND d.deleted = false")
    boolean existsByPersonId(@Param("personId") Long personId);

    List<Patient> getPatientsByIdIn(Collection<Long> ids);
}
