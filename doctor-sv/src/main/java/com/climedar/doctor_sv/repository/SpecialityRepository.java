package com.climedar.doctor_sv.repository;

import com.climedar.doctor_sv.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>, JpaSpecificationExecutor<Speciality> {
}
