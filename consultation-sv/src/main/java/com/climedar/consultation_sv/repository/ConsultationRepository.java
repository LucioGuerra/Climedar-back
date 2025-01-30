package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>,
        JpaSpecificationExecutor<Consultation> {
}
