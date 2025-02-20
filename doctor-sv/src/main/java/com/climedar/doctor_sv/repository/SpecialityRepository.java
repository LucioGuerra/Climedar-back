package com.climedar.doctor_sv.repository;

import com.climedar.doctor_sv.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>, JpaSpecificationExecutor<Speciality> {
    @Query("SELECT s.name FROM Speciality s")
    Set<String> getAllSpecialitiesNames();

    @Query("SELECT s FROM Speciality s WHERE s.id IN :ids")
    Set<Speciality> findByIdIn(Set<Long> ids);
}
