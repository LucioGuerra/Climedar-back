package com.climedar.medical_service_sv.resolver;

import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.service.MedicalService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class MedicalResolver {

    private final MedicalService medicalService;

    @QueryMapping
    public MedicalServiceModel getMedicalServiceById(@Argument Long id) {
        return medicalService.getMedicalServiceById(id);
    }

    @QueryMapping
    public MedicalServicePage getAllMedicalServices(@Argument PageRequestInput input) {
        return medicalService.adapterGetAllMedicalServices(input);
    }

    @MutationMapping
    public MedicalServiceModel createMedicalService(@Argument MedicalServiceModel input) {
        return medicalService.createMedicalService(input);
    }

    @MutationMapping
    public MedicalServiceModel updateMedicalService(@Argument Long id, @Argument MedicalServiceModel input) {
        return medicalService.updateMedicalService(id, input);
    }

    @MutationMapping
    public Boolean deleteMedicalService(@Argument Long id) {
        return medicalService.deleteMedicalService(id);
    }
}
