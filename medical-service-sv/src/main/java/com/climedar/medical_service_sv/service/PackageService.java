package com.climedar.medical_service_sv.service;

import com.climedar.library.exception.ClimedarException;
import com.climedar.medical_service_sv.dto.request.CreatePackageDTO;
import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.repository.MedicalPackageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class PackageService {

    private final MedicalPackageRepository medicalPackageRepository;
    private final MedicalPackageMapper medicalPackageMapper;
    private final MedicalService medicalService;

    public MedicalPackageModel getPackageById(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    public Page<MedicalPackageModel> getAllPackages(Pageable pageable) {
        Specification<MedicalPackageEntity> specification = Specification.where((root, query, cb) -> cb.equal(root.get("deleted"), false));
        Page<MedicalPackageEntity> medicalPackageEntities = medicalPackageRepository.findAll(specification, pageable);
        return medicalPackageEntities.map(medicalPackageMapper::toModel);

    }

    @Transactional
    public MedicalPackageModel createPackage(CreatePackageDTO createPackageDTO) {
        MedicalPackageEntity medicalPackageEntity = new MedicalPackageEntity();
        medicalPackageEntity.setName(createPackageDTO.name());
        for (Long serviceId : createPackageDTO.servicesIds()) {
            MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);
            medicalPackageEntity.addService(medicalServiceEntity);
        }
        medicalPackageEntity.setCode(this.generateCode(medicalPackageEntity.getServices()));
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

    private String generateCode(Set<MedicalServiceEntity> services) {
        String date = new SimpleDateFormat("yyyy").format(new Date());

        StringBuilder initialServices = new StringBuilder();
        for (MedicalServiceEntity service : services) {
            String initial = service.getServiceType().toString().substring(0, 2).toUpperCase();
            initialServices.append(initial).append("-");
        }

        if (!initialServices.isEmpty()) {
            initialServices.setLength(initialServices.length() - 1);
        }

        Long count = medicalPackageRepository.count();
        String number = String.format("%05d", count);
        return String.format("MP-%s-%s-%s", date, initialServices, number);
    }

    public Boolean checkIfPackageExists(Long id) {
        Optional<MedicalPackageEntity> medicalPackageEntity = medicalPackageRepository.findByIdAndNotDeleted(id);
        return medicalPackageEntity.isPresent();
    }
}
