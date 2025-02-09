package com.climedar.consultation_sv.mapper;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.medical_service.MedicalPackageModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServiceModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.library.exception.ClimedarException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") //todo: poner los demas mappers para mappear los servicios
public interface ConsultationMapper {


    @Mapping(source = "consultation.id", target = "id")
    @Mapping(source = "shift.date", target = "date")
    @Mapping(source = "shift.startTime", target = "startTime")
    @Mapping(source = "consultation.description", target = "description")
    @Mapping(source = "consultation.finalPrice", target = "finalPrice")
    @Mapping(source = "consultation.observation", target = "observation")
    @Mapping(expression = "java(this.getPatient(consultation))", target = "patient")
    @Mapping(expression = "java(this.getDoctor(shift))", target = "doctor")
    @Mapping(expression = "java(this.getMedicalServices(consultation))", target = "medicalServicesModel")
    ConsultationModel toModel(Consultation consultation, Shift shift);


    @Mapping(target = "medicalServicesCode", ignore = true)
    @Mapping(target = "finalPrice", ignore = true)
    @Mapping(target = "id", ignore = true)
    Consultation toEntity(CreateConsultationDTO createConsultationDTO, Shift shift);

    @Mapping(target = "shiftId", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "medicalServicesCode", source = "consultationDTO.medicalServicesId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateConsultationDTO consultationDTO, @MappingTarget Consultation consultation);

    default Doctor getDoctor(Shift shift) {
        return new Doctor(shift.getDoctor().getId());
    }

    default MedicalServicesModel getMedicalServices(Consultation consultation) {
        if (consultation.getMedicalServicesCode().startsWith("MP")){
            return new MedicalPackageModel(consultation.getMedicalServicesCode());
        }else {
            return new MedicalServiceModel(consultation.getMedicalServicesCode());
        }
    }

    default Patient getPatient(Consultation consultation) {
        return new Patient(consultation.getPatientId());
    }
}
