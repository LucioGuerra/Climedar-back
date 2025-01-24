package com.climedar.medical_service_sv.resolver;

import com.climedar.medical_service_sv.adapter.GraphqlResolverAdapter;
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

    private final GraphqlResolverAdapter graphqlAdapter;

    @QueryMapping
    public MedicalPackageModel getMedicalPackageById(@Argument Long id) {
        return graphqlAdapter.getPackageById(id);
    }

    @QueryMapping
    public MedicalPackagePage getAllMedicalPackages(@Argument PageRequestInput input) {
        return graphqlAdapter.getAllPackages(input);
    }

    @MutationMapping
    public MedicalPackageModel createMedicalPackage(@Argument List<Long> servicesIds) {
        return graphqlAdapter.createPackage(servicesIds);
    }

    @MutationMapping
    public Boolean deleteMedicalPackage(@Argument Long id) {
        return graphqlAdapter.deletePackage(id);
    }

    @MutationMapping
    public MedicalPackageModel addServiceToMedicalPackage(@Argument Long id, @Argument Long serviceId) {
        return graphqlAdapter.addServiceToPackage(id, serviceId);
    }

    @MutationMapping
    public MedicalPackageModel removeServiceFromMedicalPackage(@Argument Long id, @Argument Long serviceId) {
        return graphqlAdapter.removeServiceFromPackage(id, serviceId);
    }
}
