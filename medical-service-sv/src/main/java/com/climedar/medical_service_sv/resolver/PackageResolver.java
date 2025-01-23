package com.climedar.medical_service_sv.resolver;

import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalPackagePage;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
public class PackageResolver {

    private final PackageService packageService;

    @QueryMapping
    public MedicalPackageModel getMedicalPackageById(@Argument Long id) {
        return packageService.getPackageById(id);
    }

    @QueryMapping
    public MedicalPackagePage getAllMedicalPackages(@Argument PageRequestInput input) {
        return packageService.adapterGetAllPackages(input);
    }

    @MutationMapping
    public MedicalPackageModel createMedicalPackage(@Argument List<Long> servicesIds) {
        return packageService.createPackage(servicesIds);
    }

    @MutationMapping
    public void deleteMedicalPackage(@Argument Long id) {
        packageService.deletePackage(id);
    }

    @MutationMapping
    public MedicalPackageModel addServiceToMedicalPackage(@Argument Long id, @Argument Long serviceId) {
        return packageService.addServiceToPackage(id, serviceId);
    }

    @MutationMapping
    public MedicalPackageModel removeServiceFromMedicalPackage(@Argument Long id, @Argument Long serviceId) {
        return packageService.removeServiceFromPackage(id, serviceId);
    }
}
