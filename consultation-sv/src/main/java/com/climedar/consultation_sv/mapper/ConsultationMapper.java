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

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
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
    Consultation toEntity(CreateConsultationDTO createConsultationDTO);



    default Doctor getDoctor(Shift shift) {
        return new Doctor(shift.getDoctor().getId());
    }

    default List<MedicalServicesModel> getMedicalServices(Consultation consultation) {
        List<String> medicalServicesCode = consultation.getMedicalServicesCode();
        List<MedicalServicesModel> medicalServicesModels = new ArrayList<>();
        for (String code: medicalServicesCode){
            if (code.startsWith("MP")){
                medicalServicesModels.add(new MedicalPackageModel(code));
            }else {
                medicalServicesModels.add(new MedicalServiceModel(code));
            }
        }
        return medicalServicesModels;
    }

    default Patient getPatient(Consultation consultation) {
        return new Patient(consultation.getPatientId());
    }
}
