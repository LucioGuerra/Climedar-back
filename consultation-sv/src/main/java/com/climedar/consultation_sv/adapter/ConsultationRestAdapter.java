package com.climedar.consultation_sv.adapter;

import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServiceModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.MedicalServicesRepository;
import com.climedar.consultation_sv.repository.PatientRepository;
import com.climedar.consultation_sv.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ConsultationRestAdapter {

    private final ConsultationService consultationService;
    private final PatientRepository patientRepository;
    private final MedicalServicesRepository medicalServicesRepository;

    public ConsultationModel getConsultationById(Long id){
        ConsultationModel consultation = consultationService.getConsultationById(id);
        consultation.setPatient(patientRepository.findById(consultation.getPatient().getId()));

        Set<String> medicalServicesCodes =
                consultation.getMedicalServicesModel().stream().map(MedicalServicesModel::getCode).collect(Collectors.toSet());
        List<MedicalServicesWrapped> medicalServices = medicalServicesRepository.findAllByCode(medicalServicesCodes);
        consultation.setMedicalServicesModel(medicalServices.stream().map(MedicalServicesWrapped::getMedicalServices).toList());

        System.out.println("Consultation: " + consultation.toString());

        return consultation;
    }
}
