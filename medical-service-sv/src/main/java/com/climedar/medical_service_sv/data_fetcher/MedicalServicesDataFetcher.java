package com.climedar.medical_service_sv.data_fetcher;

import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsTypeResolver;

@DgsComponent
public class MedicalServicesDataFetcher {

    @DgsTypeResolver(name = "MedicalServicesModel")
    public String resolveType(Object object) {
        if (object instanceof MedicalServiceModel) {
            return "MedicalServiceModel";
        }
        if (object instanceof MedicalPackageModel) {
            return "MedicalPackageModel";
        }
        return null;
    }
}
