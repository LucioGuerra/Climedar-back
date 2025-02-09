package com.climedar.medical_service_sv.data_fetcher;

import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.model.MedicalServicesModel;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsTypeResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DgsComponent
public class MedicalServicesDataFetcher {

   /*@DgsTypeResolver(name = "MedicalServicesModel")
    public String resolveMedicalServicesType(MedicalServicesModel model) {
        if (model instanceof MedicalServiceModel) {
            log.info("MedicalServiceModel");
            return "MedicalServiceModel";
        }
        if (model instanceof MedicalPackageModel) {
            log.info("MedicalPackageModel");
            return "MedicalPackageModel";
        }
        throw new IllegalArgumentException("Unknown type: " + model.getClass());
    }*/
}
