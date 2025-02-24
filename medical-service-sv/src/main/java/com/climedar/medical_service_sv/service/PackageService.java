package com.climedar.medical_service_sv.service;

import com.climedar.library.exception.ClimedarException;
import com.climedar.medical_service_sv.dto.request.CreatePackageDTO;
import com.climedar.medical_service_sv.dto.request.UpdatePackageDTO;
import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.external.model.Speciality;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.repository.MedicalPackageRepository;
import com.climedar.medical_service_sv.repository.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Service
public class PackageService {

    private final MedicalPackageRepository medicalPackageRepository;
    private final MedicalPackageMapper medicalPackageMapper;
    private final MedicalService medicalService;
    private final SpecialityRepository specialityRepository;

    public MedicalPackageModel getPackageById(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        Set<MedicalServiceModel> services = new HashSet<>();
        for (MedicalServiceEntity service : medicalPackageEntity.getServices()) {
            services.add(medicalService.mapToModel(service));
        }
        MedicalPackageModel model = medicalPackageMapper.toModel(medicalPackageEntity);
        model.setServices(services);
        return model;
    }

    public Page<MedicalPackageModel> getAllPackages(Pageable pageable, Long specialityId, String name) {
        Specification<MedicalPackageEntity> specification = Specification.where((root, query, cb) -> cb.equal(root.get("deleted"), false));
        if (specialityId != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("specialityId"), specialityId));
        }
        if (name != null) {
            specification = specification.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        Page<MedicalPackageEntity> medicalPackageEntities = medicalPackageRepository.findAll(specification, pageable);
        return medicalPackageEntities.map(medicalPackageMapper::toModel);

    }

    @Transactional
    public MedicalPackageModel createPackage(CreatePackageDTO createPackageDTO) {
        MedicalPackageEntity medicalPackageEntity = new MedicalPackageEntity();
        medicalPackageEntity.setName(createPackageDTO.name());
        medicalPackageEntity.setSpecialityId(createPackageDTO.specialityId());
        Speciality speciality = specialityRepository.getSpecialityById(createPackageDTO.specialityId());

        for (Long serviceId : createPackageDTO.servicesIds()) {
            MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);
            if (!medicalServiceEntity.getSpecialityId().equals(createPackageDTO.specialityId())) {
                throw new ClimedarException("SERVICE_SPECIALITY_MISMATCH", "Service speciality does not match package speciality");
            }
            medicalPackageEntity.addService(medicalServiceEntity);
        }

        medicalPackageEntity.setCode(this.generateCode(speciality));
        medicalPackageEntity = medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }


    public Boolean deletePackage(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Package not found with id: " + id));
        medicalPackageEntity.setDeleted(true);
        medicalPackageRepository.save(medicalPackageEntity);
        return true;
    }

    //todo: Poder agregare una funcion fallback para agregar los que son de la especialidad
    @Transactional
    public MedicalPackageModel updatePackage(Long id, UpdatePackageDTO packageDTO) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Package not found with id: " + id));
        if (packageDTO.name() != null) {
            medicalPackageEntity.setName(packageDTO.name());
        }
        if (!packageDTO.servicesIds().isEmpty()) {
            medicalPackageEntity.getServices().clear();
            for (Long serviceId : packageDTO.servicesIds()) {
                MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);
                if (!medicalServiceEntity.getSpecialityId().equals(medicalPackageEntity.getSpecialityId())) {
                    throw new ClimedarException("SERVICE_SPECIALITY_MISMATCH", "Service speciality does not match package speciality");
                }
                medicalPackageEntity.addService(medicalServiceEntity);
            }
        }

        medicalPackageRepository.save(medicalPackageEntity);

        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    @Transactional
    public MedicalPackageModel addServiceToPackage(Long packageId, Long serviceId) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(packageId).orElseThrow(() -> new EntityNotFoundException("Package not found with id: " + packageId));
        MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);

        if (medicalPackageEntity.getServices().contains(medicalServiceEntity)) {
            throw new ClimedarException("SERVICE_ALREADY_EXIST", "Service already exists in package");
        }

        medicalPackageEntity.addService(medicalServiceEntity);
        medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    @Transactional
    public MedicalPackageModel removeServiceFromPackage(Long packageId, Long serviceId) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(packageId).orElseThrow(() -> new RuntimeException("Package not found with id: " + packageId));
        MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);

        if (!medicalPackageEntity.getServices().contains(medicalServiceEntity)) {
            throw new ClimedarException("SERVICE_NOT_EXIST", "Service does not exist in package");
        }

        medicalPackageEntity.removeService(medicalServiceEntity);
        medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    @EventListener
    public void handleMedicalServiceDeleted(MedicalServiceEntity medicalServiceEntity) {
        List<MedicalPackageEntity> medicalPackageEntities = medicalPackageRepository.findByServiceId(medicalServiceEntity.getId());
        for (MedicalPackageEntity medicalPackageEntity : medicalPackageEntities) {
            medicalPackageEntity.removeService(medicalServiceEntity);
            medicalPackageRepository.save(medicalPackageEntity);
        }
    }

    private String generateCode(Speciality specialityModel) {
        String date = new SimpleDateFormat("yyyy").format(new Date());


        String specialityName = specialityModel.getName().toUpperCase();
        String speciality;
        if(specialityName.length() >= 3){
            speciality = specialityName.substring(0, 3);
        }else {
            speciality = String.format("%-3s", specialityModel).replace(' ', 'X');
        }

        Long count = medicalPackageRepository.count();
        String number = String.format("%05d", count);
        return String.format("MP-%s-%s-%s", date, speciality, number);
    }

    public Boolean checkIfPackageExists(Long id) {
        Optional<MedicalPackageEntity> medicalPackageEntity = medicalPackageRepository.findByIdAndNotDeleted(id);
        return medicalPackageEntity.isPresent();
    }

    public MedicalPackageModel getPackageByCode(String code) {
        MedicalPackageEntity entity = medicalPackageRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Package not found with code: " + code));
        return medicalPackageMapper.toModel(entity);
    }
}
