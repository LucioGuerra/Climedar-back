package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.CreateOvertimeConsultationDTO;
import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.doctor.ShiftState;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.mapper.ConsultationMapper;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.*;
import com.climedar.consultation_sv.specification.ConsultationSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ConsultationMapper consultationMapper;
    private final ShiftRepository shiftRepository;
    private final PatientRepository patientRepository;
    private final MedicalServicesRepository medicalServicesRepository;

    public Float getConsultationPrice(Set<Long> servicesIds, Long patientId) {
        Patient patient = null;
        if (patientId != null) {
            patient = patientRepository.findById(patientId);
        }
        if (servicesIds.isEmpty()) {
            return 0f;
        }
        List<MedicalServicesWrapped> medicalServicesWrappeds = medicalServicesRepository.findAllById(servicesIds);
        List<MedicalServicesModel> medicalServicesModels = medicalServicesWrappeds.stream().map(MedicalServicesWrapped::getMedicalServices).toList();
        return calculateFinalPrice(medicalServicesModels, patient).floatValue();
    }

    public ConsultationModel getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        Shift shift = shiftRepository.findById(consultation.getShiftId());
        return consultationMapper.toModel(consultation, shift);
    }

    public Page<ConsultationModel> getAllConsultations(Pageable pageable, Long patientId, Long doctorId,
                                                       LocalDate date, LocalTime startTime, LocalTime fromTime, LocalTime toTime,
                                                       List<String> medicalServicesCode, String description,
                                                       String observation) {

        List<Long> shiftId = new ArrayList<>();
        if (!(doctorId == null && date == null)) {
            shiftId = shiftRepository.getAllShift(doctorId, date).stream().map(Shift::getId).toList();
        }
        Specification<Consultation> specification = Specification.where(ConsultationSpecification.ByPatientId(patientId))
                .and(ConsultationSpecification.ByShiftId(shiftId))
                .and(ConsultationSpecification.ByMedicalServicesCode(medicalServicesCode))
                .and(ConsultationSpecification.ByTime(startTime, fromTime, toTime)) //todo: Este specification rompe todo por que ya no existe startime
                .and(ConsultationSpecification.likeDescription(description))
                .and(ConsultationSpecification.likeObservation(observation))
                .and(ConsultationSpecification.byDeleted(false));

        Page<Consultation> consultations = consultationRepository.findAll(specification, pageable);

        Set<Long> shiftIds = new HashSet<>();

        consultations.forEach(consultation -> {
            shiftIds.add(consultation.getShiftId());
        });

        List<Shift> shifts = shiftRepository.findAllById(shiftIds);
        Map<Long, Shift> shiftMap = shifts.stream().collect(Collectors.toMap(Shift::getId, Function.identity()));

        return consultations.map(consultation -> {
            Shift shift = shiftMap.get(consultation.getShiftId());
            return consultationMapper.toModel(consultation, shift);
        });
    }

    @Transactional
    public ConsultationModel createConsultation(CreateConsultationDTO createConsultationDTO) {

        List<MedicalServicesWrapped> medicalServicesWrappeds =
                medicalServicesRepository.findAllById(createConsultationDTO.medicalServicesId());
        List<MedicalServicesModel> medicalServicesModels = medicalServicesWrappeds.stream().map(MedicalServicesWrapped::getMedicalServices).toList();

        Doctor doctor = shiftRepository.findDoctorById((createConsultationDTO.doctorId()));
        for (MedicalServicesModel medicalServicesModel : medicalServicesModels) {
            if (!doctor.getSpeciality().getId().equals(medicalServicesModel.getSpeciality().getId())) {
                throw new ClimedarException("DOCTOR_DOESNT_PROVIDE_THESE_SERVICES", "Doctor speciality and medical " +
                        "service " +
                        "speciality " +
                        "doesn't match");
            }
        }

        Shift shift;
        if (createConsultationDTO.shiftId() != null) {
            shift = shiftRepository.findById(createConsultationDTO.shiftId());
            if (shift.getState() == ShiftState.OCCUPIED) {
                throw new ClimedarException("SHIFT_IS_OCCUPIED", "Shift is already occupied");
            }
        } else {
            Duration duration = Duration.ZERO;
            for (MedicalServicesModel medicalServicesModel : medicalServicesModels) {
                duration = duration.plus(medicalServicesModel.getEstimatedDuration());
            }
            shift = shiftRepository.createShift(createConsultationDTO.doctorId(), duration);
        }




        Patient patient = patientRepository.findById(createConsultationDTO.patientId());

        List<String> medicalServicesCodes = medicalServicesModels.stream().map(MedicalServicesModel::getCode).toList();

        Consultation consultation = consultationMapper.toEntity(createConsultationDTO);
        consultation.setShiftId(shift.getId());
        consultation.setMedicalServicesCode(medicalServicesCodes);
        consultation.setFinalPrice(calculateFinalPrice(medicalServicesModels, patient));
        consultationRepository.save(consultation);

        shiftRepository.occupyShift(shift.getId());
        return consultationMapper.toModel(consultation, shift);
    }


    @Transactional
    public ConsultationModel updateConsultation(Long id, UpdateConsultationDTO updateConsultationDTO) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        Shift shift;

        if (updateConsultationDTO.shiftId() != null) {
            shift = shiftRepository.findById(updateConsultationDTO.shiftId());
            if (shift.getState() == ShiftState.OCCUPIED) {
                throw new ClimedarException("SHIFT_IS_OCCUPIED", "Shift is already occupied");
            }
            shiftRepository.occupyShift(shift.getId());
            shiftRepository.clearShift(consultation.getShiftId());
            consultation.setShiftId(shift.getId());
        }else {
            shift = shiftRepository.findById(consultation.getShiftId());
        }
        if (updateConsultationDTO.medicalServicesId() != null) {
            List<MedicalServicesWrapped> medicalServicesWrappeds =
                    medicalServicesRepository.findAllById(updateConsultationDTO.medicalServicesId());
            List<MedicalServicesModel> medicalServicesModels = medicalServicesWrappeds.stream().map(MedicalServicesWrapped::getMedicalServices).toList();
            List<String> medicalServicesCodes = medicalServicesModels.stream().map(MedicalServicesModel::getCode).toList();
            consultation.setMedicalServicesCode(medicalServicesCodes);
            consultation.setFinalPrice(calculateFinalPrice(medicalServicesModels, patientRepository.findById(consultation.getPatientId())));
        }
        if (updateConsultationDTO.description() != null) {
            consultation.setDescription(updateConsultationDTO.description());
        }
        if (updateConsultationDTO.observation() != null) {
            consultation.setObservation(updateConsultationDTO.observation());
        }

        consultationRepository.save(consultation);

        return consultationMapper.toModel(consultation, shift);
    }

    public Boolean deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        consultation.setDeleted(true);
        consultationRepository.save(consultation);
        shiftRepository.clearShift(consultation.getShiftId());
        return true;
    }

    private Double calculateFinalPrice(List<MedicalServicesModel> medicalServicesModel, Patient patient) {
        if (patient == null || patient.getMedicalSecure() == null) {
            return medicalServicesModel.stream().mapToDouble(MedicalServicesModel::getPrice).sum();
        }
        return medicalServicesModel.stream().mapToDouble(MedicalServicesModel::getPrice).sum() * 0.80;
    }

}
