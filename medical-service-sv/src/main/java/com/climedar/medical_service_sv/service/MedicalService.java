package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.request.SortOption;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.mapper.PageInfoMapper;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
import com.climedar.medical_service_sv.repository.MedicalServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MedicalService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;
    private final PageInfoMapper pageInfoMapper;

    public MedicalServiceModel getMedicalServiceById(Long id) {
       MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
       System.out.println("MedicalService.getMedicalServiceById");

       return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public Page<MedicalServiceModel> getAllMedicalServices(Pageable pageable) {
        Specification<MedicalServiceEntity> specification = Specification.where((root, query, cb) -> cb.equal(root.get("deleted"), false));

        Page<MedicalServiceEntity> medicalServiceEntities = medicalServiceRepository.findAll(specification, pageable);

        return medicalServiceEntities.map(medicalServiceMapper::toModel);
    }

    public MedicalServiceModel createMedicalService(MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceMapper.toEntity(medicalServiceModel);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public MedicalServiceModel updateMedicalService(Long id, MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));

        updateEntityFields(medicalServiceModel, medicalServiceEntity);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public void deleteMedicalService(Long id) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
        medicalServiceEntity.setDeleted(true);
        medicalServiceRepository.save(medicalServiceEntity);
    }

    private void updateEntityFields(MedicalServiceModel medicalServiceModel, MedicalServiceEntity medicalServiceEntity) {
        Optional.ofNullable(medicalServiceModel.getName())
                .ifPresent(medicalServiceEntity::setName);
        Optional.ofNullable(medicalServiceModel.getPrice())
                .ifPresent(medicalServiceEntity::setPrice);
        Optional.ofNullable(medicalServiceModel.getServiceType())
                .ifPresent(medicalServiceEntity::setServiceType);
        Optional.ofNullable(medicalServiceModel.getDescription())
                .ifPresent(medicalServiceEntity::setDescription);
    }

    public MedicalServiceEntity getMedicalServiceEntityById(Long id) {
        return medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
    }

    public MedicalServicePage adapterGetAllMedicalServices(PageRequestInput pageRequestInput) {
        Sort sort = Sort.unsorted();
        if (pageRequestInput.getSortOption() != null) {
            SortOption sortOption = pageRequestInput.getSortOption();
            sort = Sort.by(
                    Sort.Direction.fromString(sortOption.getDirection().name()),
                    sortOption.getField()
            );
        }

        Pageable pageable = PageRequest.of(pageRequestInput.getPage(), pageRequestInput.getSize(), sort);

        Page<MedicalServiceModel> medicalServiceModels = getAllMedicalServices(pageable);

        MedicalServicePage medicalServicePage = new MedicalServicePage();
        medicalServicePage.setPageInfo(pageInfoMapper.toPageInfo(medicalServiceModels));
        medicalServicePage.setServices(medicalServiceModels.getContent());
        return medicalServicePage;
    }
}
