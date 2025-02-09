package com.climedar.consultation_sv.data_fetcher;

import com.climedar.consultation_sv.adapter.ConsultationGraphqlAdapter;
import com.climedar.consultation_sv.dto.request.ConsultationSpecificationDTO;
import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.dto.response.ConsultationPage;
import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.library.dto.request.PageRequestInput;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.Map;

@AllArgsConstructor
@DgsComponent
public class ConsultationDataFetcher {

    private final ConsultationGraphqlAdapter consultationAdapter;

    @DgsQuery
    public ConsultationPage getAllConsultations(@InputArgument PageRequestInput pageRequest,
                                                @InputArgument ConsultationSpecificationDTO specification) {
        return consultationAdapter.getAllConsultations(pageRequest, specification);
    }

    @DgsQuery
    public ConsultationModel getConsultationById(@InputArgument Long id) {
        return consultationAdapter.getConsultationById(id);
    }

    @DgsMutation
    public ConsultationModel createConsultation(@InputArgument CreateConsultationDTO consultation) {
        return consultationAdapter.createConsultation(consultation);
    }

    @DgsMutation
    public ConsultationModel updateConsultation(@InputArgument Long id, @InputArgument UpdateConsultationDTO consultation) {
        return consultationAdapter.updateConsultation(id, consultation);
    }

    @DgsMutation
    public Boolean deleteConsultation(@InputArgument Long id) {
        return consultationAdapter.deleteConsultation(id);
    }

    @DgsEntityFetcher(name = "Consultation")
    public ConsultationModel getConsultation(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return consultationAdapter.getConsultationById(id);
    }

    @DgsEntityFetcher(name = "Doctor")
    public Doctor getDoctor(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return new Doctor(id);
    }

    @DgsEntityFetcher(name = "Patient")
    public Patient getPatient(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return new Patient(id);
    }

    @DgsEntityFetcher(name = "MedicalServices")
    public MedicalServices getMedicalServices(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return new MedicalServices() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public Double getPrice() {
                return 0.0;
            }

            @Override
            public Duration getEstimatedDuration() {
                return null;
            }
        };
    }
}
