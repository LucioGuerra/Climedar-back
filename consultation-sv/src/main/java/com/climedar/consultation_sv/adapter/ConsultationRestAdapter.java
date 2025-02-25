package com.climedar.consultation_sv.adapter;

import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.feign.MedicalServicesRepository;
import com.climedar.consultation_sv.repository.feign.PatientRepository;
import com.climedar.consultation_sv.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

        System.out.println("Consultation: " + consultation);

        return consultation;
    }

    public List<ConsultationModel> getConsultationsByIds(Set<Long> ids) {
        List<ConsultationModel> consultations = consultationService.getConsultationsByIds(ids);

        Set<Long> patientIds = consultations.stream()
                .map(consultation -> consultation.getPatient().getId())
                .collect(Collectors.toSet());

        List<Patient> patients = patientRepository.findAllById(patientIds);
        Map<Long, Patient> patientMap = patients.stream()
                .collect(Collectors.toMap(Patient::getId, Function.identity()));

        Set<String> allServiceCodes = consultations.stream()
                .flatMap(consultation -> consultation.getMedicalServicesModel().stream()
                        .map(MedicalServicesModel::getCode))
                .collect(Collectors.toSet());

        List<MedicalServicesWrapped> wrappedServices = medicalServicesRepository.findAllByCode(allServiceCodes);
        Map<String, MedicalServicesModel> medicalServiceMap = wrappedServices.stream()
                .collect(Collectors.toMap(
                        ws -> ws.getMedicalServices().getCode(),
                        MedicalServicesWrapped::getMedicalServices
                ));

        for (ConsultationModel consultation : consultations) {
            Long patientId = consultation.getPatient().getId();
            consultation.setPatient(patientMap.get(patientId));

            List<MedicalServicesModel> services = consultation.getMedicalServicesModel().stream()
                    .map(MedicalServicesModel::getCode)
                    .map(medicalServiceMap::get)
                    .collect(Collectors.toList());

            consultation.setMedicalServicesModel(services);
        }

        return consultations;
    }
}
