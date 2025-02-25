package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>,
        JpaSpecificationExecutor<Consultation> {

    Integer countByShiftId(Long shiftId);

    @Query("SELECT c FROM Consultation c WHERE c.id = :id AND c.deleted = false")
    Optional<Consultation> findByIdAndNotDeleted(Long id);

    Consultation findByShiftId(Long shiftId);

    List<Consultation> findAllByShiftIdIn(List<Long> list);
}
