package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.event.received.ConfirmedPayEvent;
import com.climedar.consultation_sv.external.event.received.ShiftCanceledEvent;
import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.doctor.ShiftState;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.mapper.ConsultationMapper;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.*;
import com.climedar.consultation_sv.repository.feign.MedicalServicesRepository;
import com.climedar.consultation_sv.repository.feign.PatientRepository;
import com.climedar.consultation_sv.repository.feign.ShiftRepository;
import com.climedar.consultation_sv.specification.ConsultationSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final EmailEventService emailEventService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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
        Optional<Shift> shift = shiftRepository.findById(consultation.getShiftId());
        if (shift.isEmpty()) {
            ConsultationModel consultationModel = consultationMapper.toModelWithoutShift(consultation);
            consultationModel.setDoctor(new Doctor(consultation.getDoctorId()));
            return consultationModel;
        }
        ConsultationModel consultationModel = consultationMapper.toModelWithShift(consultation, shift.get());
        return consultationModel;
    }

    public Page<ConsultationModel> getAllConsultations(Pageable pageable, Long patientId, Long doctorId,
                                                       LocalDate date, LocalTime startTime, LocalTime fromTime, LocalTime toTime,
                                                       List<String> medicalServicesCode, String description,
                                                       String observation) {

        List<Long> shiftId = new ArrayList<>();
        if ((date != null || startTime != null || fromTime != null || toTime != null)) {
            shiftId = shiftRepository.getAllShift(date, startTime, fromTime, toTime).stream().map(Shift::getId).toList();
        }
        Specification<Consultation> specification = Specification.where(ConsultationSpecification.ByPatientId(patientId))
                .and(ConsultationSpecification.ByShiftId(shiftId))
                .and(ConsultationSpecification.ByMedicalServicesCode(medicalServicesCode))
                .and(ConsultationSpecification.likeDescription(description))
                .and(ConsultationSpecification.likeObservation(observation))
                .and(ConsultationSpecification.ByDoctorId(doctorId))
                .and(ConsultationSpecification.byDeleted(false));

        Page<Consultation> consultations = consultationRepository.findAll(specification, pageable);



        Set<Long> shiftIds = new HashSet<>();

        consultations.forEach(consultation -> {
            if (consultation.getShiftId() != null){
                shiftIds.add(consultation.getShiftId());
            }
        });

        List<Shift> shifts = shiftRepository.findAllById(shiftIds);
        Map<Long, Shift> shiftMap = shifts.stream().collect(Collectors.toMap(Shift::getId, Function.identity()));

        return consultations.map(consultation -> {
            if (consultation.getShiftId() == null || consultation.getShiftId() == -1){
                ConsultationModel consultationModel = consultationMapper.toModelWithoutShift(consultation);
                consultationModel.setDoctor(new Doctor(consultation.getDoctorId()));
                return consultationModel;
            }
            Shift shift = shiftMap.get(consultation.getShiftId());
            return consultationMapper.toModelWithShift(consultation, shift);
        });
    }

    @Transactional
    public ConsultationModel createConsultation(CreateConsultationDTO createConsultationDTO) {

        List<MedicalServicesWrapped> medicalServicesWrappeds =
                medicalServicesRepository.findAllById(createConsultationDTO.medicalServicesId());
        List<MedicalServicesModel> medicalServicesModels = medicalServicesWrappeds.stream().map(MedicalServicesWrapped::getMedicalServices).toList();


        Shift shift;
        if (createConsultationDTO.shiftId() != null) {
            shift = shiftRepository.findById(createConsultationDTO.shiftId()).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + createConsultationDTO.shiftId()));
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

        Doctor doctor = shift.getDoctor();
        for (MedicalServicesModel medicalServicesModel : medicalServicesModels) {
            if (!doctor.getSpeciality().getId().equals(medicalServicesModel.getSpeciality().getId())) {
                throw new ClimedarException("DOCTOR_DOESNT_PROVIDE_THESE_SERVICES", "Doctor speciality and medical " +
                        "service " +
                        "speciality " +
                        "doesn't match");
            }
        }


        Patient patient = patientRepository.findById(createConsultationDTO.patientId());

        List<String> medicalServicesCodes = medicalServicesModels.stream().map(MedicalServicesModel::getCode).toList();

        Consultation consultation = consultationMapper.toEntity(createConsultationDTO);
        consultation.setShiftId(shift.getId());
        consultation.setMedicalServicesCode(medicalServicesCodes);
        consultation.setFinalPrice(calculateFinalPrice(medicalServicesModels, patient));
        consultationRepository.save(consultation);

        shiftRepository.occupyShift(shift.getId());
        return consultationMapper.toModelWithShift(consultation, shift);
    }


    @Transactional
    public ConsultationModel updateConsultation(Long id, UpdateConsultationDTO updateConsultationDTO) {
        Consultation consultation = consultationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + id));
        Shift shift;

        if (updateConsultationDTO.shiftId() != null) {
            shift = shiftRepository.findById(updateConsultationDTO.shiftId()).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + updateConsultationDTO.shiftId()));
            if (shift.getState() == ShiftState.OCCUPIED) {
                throw new ClimedarException("SHIFT_IS_OCCUPIED", "Shift is already occupied");
            }
            shiftRepository.occupyShift(shift.getId());
            shiftRepository.clearShift(consultation.getShiftId());
            consultation.setShiftId(shift.getId());
        }else {
            shift = shiftRepository.findById(consultation.getShiftId()).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + consultation.getShiftId()));
        }


        if (updateConsultationDTO.medicalServicesId() != null) {
            List<MedicalServicesWrapped> medicalServicesWrappeds =
                    medicalServicesRepository.findAllById(updateConsultationDTO.medicalServicesId());
            List<MedicalServicesModel> medicalServicesModels = medicalServicesWrappeds.stream().map(MedicalServicesWrapped::getMedicalServices).toList();
            List<String> medicalServicesCodes = medicalServicesModels.stream().map(MedicalServicesModel::getCode).toList();
            consultation.getMedicalServicesCode().clear();
            for (String code: medicalServicesCodes){
                consultation.getMedicalServicesCode().add(code);
            }
            consultation.setFinalPrice(calculateFinalPrice(medicalServicesModels, patientRepository.findById(consultation.getPatientId())));
        }
        if (updateConsultationDTO.description() != null) {
            consultation.setDescription(updateConsultationDTO.description());
        }
        if (updateConsultationDTO.observation() != null) {
            consultation.setObservation(updateConsultationDTO.observation());
        }

        consultationRepository.save(consultation);

        return consultationMapper.toModelWithShift(consultation, shift);
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

    public List<ConsultationModel> getConsultationsByIds(Set<Long> ids) {
        List<Consultation> consultations = consultationRepository.findAllById(ids);
        Set<Long> shiftIds = new HashSet<>();

        consultations.forEach(consultation -> {
            if (consultation.getShiftId() != null){
                shiftIds.add(consultation.getShiftId());
            }
        });

        List<Shift> shifts = shiftRepository.findAllById(shiftIds);
        Map<Long, Shift> shiftMap = shifts.stream().collect(Collectors.toMap(Shift::getId, Function.identity()));

        return consultations.stream().map(consultation -> {
            if (consultation.getShiftId() == null){
                ConsultationModel consultationModel = consultationMapper.toModelWithoutShift(consultation);
                consultationModel.setDoctor(new Doctor(consultation.getDoctorId()));
                return consultationModel;
            }
            Shift shift = shiftMap.get(consultation.getShiftId());
            return consultationMapper.toModelWithShift(consultation, shift);
        }).toList();
    }

    @Transactional
    @KafkaListener(topics = "confirmed-pay", groupId = "consultation-sv")
    public void consumeConfirmedPay(ConfirmedPayEvent event) {
        Consultation consultation = consultationRepository.findById(event.getConsultationId()).orElseThrow(() -> new EntityNotFoundException("Consultation not found with id: " + event.getConsultationId()));
        consultation.setPaid(true);
        consultationRepository.save(consultation);
    }

    @Transactional
    @KafkaListener(topics = "shift-canceled", groupId = "consultation-sv")
    public void consumeShiftCanceled(ShiftCanceledEvent event) {
        Consultation consultation = consultationRepository.findByShiftId(event.getShiftId());
        consultation.setShiftId(-1L);
        consultationRepository.save(consultation);
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void notifyConsultation() {
        List<Shift> shifts = shiftRepository.getShiftsByDateAndOccupied(LocalDate.now().plusDays(1).toString());
        List<Consultation> consultations = consultationRepository.findAllByShiftIdIn(shifts.stream().map(Shift::getId).toList());
        Set<Long> patientIds = consultations.stream().map(Consultation::getPatientId).collect(Collectors.toSet());
        List<Patient> patients = patientRepository.findAllById(patientIds);

        Map<Long, Patient> patientMap = patients.stream().collect(Collectors.toMap(Patient::getId, Function.identity()));

        consultations.forEach(consultation -> {
            Patient patient = patientMap.get(consultation.getPatientId());
            kafkaTemplate.send("notification", emailEventService.createConsultationNotificationEvent(consultation, patient));
        });
    }

}
