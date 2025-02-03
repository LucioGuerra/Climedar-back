package com.climedar.doctor_sv.repository;

import com.climedar.doctor_sv.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {

    @Query("SELECT s FROM Shift s WHERE s.id IN :ids AND s.deleted = false")
    List<Shift> findAllByIdAndNotDeleted(Set<Long> ids);
}
