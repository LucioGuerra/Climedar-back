package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.mapper.ConsultationMapper;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.ConsultationRepository;
import com.climedar.consultation_sv.repository.MedicalServicesRepository;
import com.climedar.consultation_sv.repository.PatientRepository;
import com.climedar.consultation_sv.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ConsultationMapper consultationMapper;
    private final ShiftRepository shiftRepository;
    private final PatientRepository patientRepository;
    private final MedicalServicesRepository medicalServicesRepository;

    public ConsultationModel getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        return consultationMapper.toModel(consultation);
    }

    public Page<ConsultationModel> getAllConsultations(Pageable pageable, Long patientId, Long doctorId,
                                                       LocalDate date, LocalTime startTime, LocalTime fromTime, LocalTime toTime,
                                                       Duration estimatedDuration, Long medicalServiceId, String description,
                                                       String observation) {


    }

    public ConsultationModel createConsultation(CreateConsultationDTO consultationDTO) {
        Shift shift = shiftRepository.findById(consultationDTO.shiftId());
        Patient patient = patientRepository.findById(consultationDTO.patientId());
        MedicalServices medicalServices = medicalServicesRepository.findById(consultationDTO.medicalServiceId());

        Consultation consultation = consultationMapper.toEntity(consultationDTO);
        consultationRepository.save(consultation);

        ConsultationModel consultationModel = consultationMapper.toModel(consultation);
        consultationModel.setDate(shift.getDate());

        return consultationMapper.toModel(consultation);
    }


}
