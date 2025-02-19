package com.climedar.medical_service_sv.adapter;

import com.climedar.medical_service_sv.dto.request.*;
import com.climedar.medical_service_sv.dto.response.MedicalPackagePage;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.mapper.PageInfoMapper;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.service.MedicalService;
import com.climedar.medical_service_sv.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GraphqlResolverAdapter {

    private final PackageService packageService;
    private final MedicalService medicalService;
    private final PageInfoMapper pageInfoMapper;

    public MedicalPackagePage getAllPackages(PageRequestInput input, Long specialityId, String name) {
        Pageable pageable = PageRequest.of(input.getPage()-1, input.getSize(), input.getSort());

        Page<MedicalPackageModel> medicalPackageModels = packageService.getAllPackages(pageable, specialityId, name);

        MedicalPackagePage medicalPackagePage = new MedicalPackagePage();
        medicalPackagePage.setPageInfo(pageInfoMapper.toPageInfo(medicalPackageModels));
        medicalPackagePage.setPackages(medicalPackageModels.getContent());
        return medicalPackagePage;
    }

    public MedicalPackageModel getPackageById(Long id) {
        return packageService.getPackageById(id);
    }

    public MedicalPackageModel createPackage(CreatePackageDTO input) {
        return packageService.createPackage(input);
    }

    public Boolean deletePackage(Long id) {
        return packageService.deletePackage(id);
    }

    public MedicalPackageModel addServiceToPackage(Long id, Long serviceId) {
        return packageService.addServiceToPackage(id, serviceId);
    }

    public MedicalPackageModel removeServiceFromPackage(Long id, Long serviceId) {
        return packageService.removeServiceFromPackage(id, serviceId);
    }

    public MedicalPackageModel updatePackage(Long id, UpdatePackageDTO input) {
        return packageService.updatePackage(id, input);
    }


    public MedicalServicePage getAllMedicalServices(PageRequestInput input, SpecificationDTO specificationDTO) {
        Pageable pageable = PageRequest.of(input.getPage()-1, input.getSize(), input.getSort());

        if (specificationDTO == null) {
            specificationDTO = new SpecificationDTO();
        }
        Page<MedicalServiceModel> medicalServiceModels = medicalService.getAllMedicalServices(pageable, specificationDTO.getName(), specificationDTO.getCode(),
                specificationDTO.getDescription(), specificationDTO.getServiceType(), specificationDTO.getSpecialityId());

        MedicalServicePage medicalServicePage = new MedicalServicePage();
        medicalServicePage.setPageInfo(pageInfoMapper.toPageInfo(medicalServiceModels));
        medicalServicePage.setServices(medicalServiceModels.getContent());
        return medicalServicePage;
    }

    public MedicalServiceModel getMedicalServiceById(Long id) {
        return medicalService.getMedicalServiceById(id);
    }

    public MedicalServiceModel createMedicalService(CreateMedicalDTO input) {
        return medicalService.createMedicalService(input);
    }

    public MedicalServiceModel updateMedicalService(Long id, UpdateMedicalServiceDTO input) {
        return medicalService.updateMedicalService(id, input);
    }

    public Boolean deleteMedicalService(Long id) {
        return medicalService.deleteMedicalService(id);
    }

    public MedicalPackageModel getPackageByCode(String code) {
        return packageService.getPackageByCode(code);
    }

    public MedicalServiceModel getMedicalServiceByCode(String code) {
        return medicalService.getMedicalServiceByCode(code);
    }
}
