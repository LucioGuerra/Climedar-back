package com.climedar.medical_service_sv.data_fetcher;

import com.climedar.medical_service_sv.adapter.GraphqlResolverAdapter;
import com.climedar.medical_service_sv.dto.request.CreatePackageDTO;
import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.request.UpdatePackageDTO;
import com.climedar.medical_service_sv.dto.response.MedicalPackagePage;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
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
    public MedicalPackagePage getAllMedicalPackages(@InputArgument PageRequestInput input, @InputArgument Long specialityId, @InputArgument String name) {
        return graphqlAdapter.getAllPackages(input, specialityId, name);
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

    @DgsMutation
    public MedicalPackageModel updateMedicalPackage(@InputArgument Long id, @InputArgument UpdatePackageDTO input) {
        return graphqlAdapter.updatePackage(id, input);
    }

    @DgsEntityFetcher(name = "MedicalPackageModel")
    public MedicalPackageModel getMedicalPackage(Map<String, Object> values) {
        String code = (String) values.get("code");
        return graphqlAdapter.getPackageByCode(code);
    }

}
