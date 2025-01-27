package com.climedar.doctor_sv.repository;

import com.climedar.doctor_sv.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    @Query("SELECT d FROM Doctor d WHERE d.personId = :personId AND d.deleted = false")
    boolean existsByPersonId(Long personId);
}
