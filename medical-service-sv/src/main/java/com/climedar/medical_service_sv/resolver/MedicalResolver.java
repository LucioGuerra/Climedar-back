package com.climedar.medical_service_sv.resolver;

import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.service.MedicalService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
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

}
