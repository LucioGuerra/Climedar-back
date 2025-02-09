package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.doctor.ShiftState;
import com.climedar.consultation_sv.external.model.medical_service.MedicalPackageModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServiceModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.mapper.ConsultationMapper;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.ConsultationRepository;
import com.climedar.consultation_sv.repository.MedicalServicesRepository;
import com.climedar.consultation_sv.repository.PatientRepository;
import com.climedar.consultation_sv.repository.ShiftRepository;
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

    public ConsultationModel getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        Shift shift = shiftRepository.findById(consultation.getShiftId());
        Patient patient = patientRepository.findById(consultation.getPatientId());
        /*return consultationMapper.toModel(consultation);*/
        //MedicalServicesModel medicalServicesModel = medicalServicesRepository.findById(consultation.getMedicalServicesId()).getMedicalServices();
        ConsultationModel consultationModel = new ConsultationModel();
        consultationModel.setId(consultation.getId());
        consultationModel.setDate(shift.getDate());
        consultationModel.setStartTime(shift.getStartTime());
        consultationModel.setDescription(consultation.getDescription());
        consultationModel.setFinalPrice(consultation.getFinalPrice());
        consultationModel.setObservation(consultation.getObservation());
        consultationModel.setDoctor(new Doctor(shift.getDoctor().getId()));
        consultationModel.setPatient(new Patient(consultation.getPatientId()));
        if (consultation.getMedicalServicesCode().startsWith("MP")){
            consultationModel.setMedicalServicesModel(new MedicalPackageModel(consultation.getMedicalServicesCode()));
        }else {
            consultationModel.setMedicalServicesModel(new MedicalServiceModel(consultation.getMedicalServicesCode()));
        }
        return consultationModel;
    }

    public Page<ConsultationModel> getAllConsultations(Pageable pageable, Long patientId, Long doctorId,
                                                       LocalDate date, LocalTime startTime, LocalTime fromTime, LocalTime toTime,
                                                       Long medicalServicesId, String description, String observation) {

        List<Long> shiftId = new ArrayList<>();
        if (!(doctorId == null && date == null)) {
            shiftId = shiftRepository.getAllShift(doctorId, date).stream().map(Shift::getId).toList();
        }
        Specification<Consultation> specification = Specification.where(ConsultationSpecification.ByPatientId(patientId))
                .and(ConsultationSpecification.ByShiftId(shiftId))
                .and(ConsultationSpecification.ByMedicalServicesId(medicalServicesId))
                .and(ConsultationSpecification.ByTime(startTime, fromTime, toTime)) //todo: Este specification rompe todo por que ya no existe startime
                .and(ConsultationSpecification.likeDescription(description))
                .and(ConsultationSpecification.likeObservation(observation))
                .and(ConsultationSpecification.byDeleted(false));

        Page<Consultation> consultations = consultationRepository.findAll(specification, pageable);

        Set<Long> shiftIds = new HashSet<>();
        //Set<Long> patientIds = new HashSet<>();
        Set<Long> medicalServiceIds = new HashSet<>();

        consultations.forEach(consultation -> {
            shiftIds.add(consultation.getShiftId());
            //patientIds.add(consultation.getPatientId());
            medicalServiceIds.add(consultation.getMedicalServicesId());
        });

        List<Shift> shifts = shiftRepository.findAllById(shiftIds);
        //List<Patient> patients = patientRepository.findAllById(patientIds);
        List<MedicalServicesModel> medicalServiceModels = medicalServicesRepository.findAllById(medicalServiceIds).stream().map(MedicalServicesWrapped::getMedicalServices).toList();


        Map<Long, Shift> shiftMap = shifts.stream().collect(Collectors.toMap(Shift::getId, Function.identity()));
        //Map<Long, Patient> patientMap = patients.stream().collect(Collectors.toMap(Patient::getId, Function.identity()));
        Map<Long, MedicalServicesModel> medicalServiceMap = medicalServiceModels.stream().collect(Collectors.toMap(MedicalServicesModel::getId, Function.identity()));

        return consultations.map(consultation -> {
            Shift shift = shiftMap.get(consultation.getShiftId());
            //Patient patient = patientMap.get(consultation.getPatientId());
            MedicalServicesModel medicalService = medicalServiceMap.get(consultation.getMedicalServicesId());
            return consultationMapper.toModel(consultation);
        });
    }

    @Transactional
    public ConsultationModel createConsultation(CreateConsultationDTO createConsultationDTO) {
        Shift shift = shiftRepository.findById(createConsultationDTO.shiftId());
        if (shift.getState() == ShiftState.OCCUPIED) {
            throw new ClimedarException("SHIFT_IS_OCCUPIED", "Shift is already occupied");
        }

        //Patient patient = patientRepository.findById(createConsultationDTO.patientId());
        Patient patient = new Patient();
        MedicalServicesModel medicalServicesModel =
                medicalServicesRepository.findById(createConsultationDTO.medicalServices()).getMedicalServices();

        Consultation consultation = consultationMapper.toEntity(createConsultationDTO, shift, medicalServicesModel);
        consultation.setFinalPrice(calculateFinalPrice(medicalServicesModel, patient));
        consultationRepository.save(consultation);

        shiftRepository.occupyShift(shift.getId());

        return consultationMapper.toModel(consultation);//todo: agregar patient
    }



    @Transactional
    public ConsultationModel updateConsultation(Long id, UpdateConsultationDTO updateConsultationDTO) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        consultationMapper.updateEntity(updateConsultationDTO, consultation);

        Shift shift = shiftRepository.findById(consultation.getShiftId());
        //Patient patient = patientRepository.findById(consultation.getPatientId());
        MedicalServicesModel medicalServicesModel = medicalServicesRepository.findById(consultation.getMedicalServicesId()).getMedicalServices();

        consultationRepository.save(consultation);

        return consultationMapper.toModel(consultation);
    }

    public Boolean deleteConsultation(Long id) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        consultation.setDeleted(true);
        consultationRepository.save(consultation);
        shiftRepository.clearShift(consultation.getShiftId());
        return true;
    }

    private Double calculateFinalPrice(MedicalServicesModel medicalServicesModel, Patient patient) {
        if (patient.getMedicalSecure() == null) {
            return medicalServicesModel.getPrice();
        }
        return medicalServicesModel.getPrice() * 0.80;
    }

}
