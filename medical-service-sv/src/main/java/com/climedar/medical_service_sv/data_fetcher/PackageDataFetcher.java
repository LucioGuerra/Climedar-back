package com.climedar.medical_service_sv.data_fetcher;

import com.climedar.medical_service_sv.adapter.GraphqlResolverAdapter;
import com.climedar.medical_service_sv.dto.request.CreatePackageDTO;
import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalPackagePage;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@DgsComponent
public class PackageDataFetcher {

    private final GraphqlResolverAdapter graphqlAdapter;

    @DgsQuery
    public MedicalPackageModel getMedicalPackageById(@InputArgument Long id) {
        return graphqlAdapter.getPackageById(id);
    }

    @DgsQuery
    public MedicalPackagePage getAllMedicalPackages(@InputArgument PageRequestInput input) {
        return graphqlAdapter.getAllPackages(input);
    }

    @DgsMutation
    public MedicalPackageModel createMedicalPackage(@InputArgument CreatePackageDTO input) {
        return graphqlAdapter.createPackage(input);
    }

    @DgsMutation
    public Boolean deleteMedicalPackage(@InputArgument Long id) {
        return graphqlAdapter.deletePackage(id);
    }

    @DgsMutation
    public MedicalPackageModel addServiceToMedicalPackage(@InputArgument Long id, @InputArgument Long serviceId) {
        return graphqlAdapter.addServiceToPackage(id, serviceId);
    }

    @DgsMutation
    public MedicalPackageModel removeServiceFromMedicalPackage(@InputArgument Long id, @InputArgument Long serviceId) {
        return graphqlAdapter.removeServiceFromPackage(id, serviceId);
    }

    @DgsEntityFetcher(name = "MedicalPackage")
    public MedicalPackageModel getMedicalPackage(MedicalPackageModel input) {
        return new MedicalPackageModel(input.getId());
    }

}
