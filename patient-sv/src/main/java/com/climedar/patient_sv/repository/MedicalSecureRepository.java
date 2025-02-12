package com.climedar.patient_sv.repository;

import com.climedar.patient_sv.entity.MedicalSecure;
import com.climedar.patient_sv.entity.Patient;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalSecureRepository extends JpaRepository<MedicalSecure, Long>, JpaSpecificationExecutor<MedicalSecure> {

}
