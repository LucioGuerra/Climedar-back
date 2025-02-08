package com.climedar.patient_sv.adapter;


import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import com.climedar.patient_sv.dto.request.specification.PatientSpecificationDTO;
import com.climedar.patient_sv.dto.response.PatientPage;
import com.climedar.patient_sv.model.PatientModel;
import com.climedar.patient_sv.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PatientGraphqlAdapter {

    private final PatientService patientService;
    private final PageInfoMapper pageInfoMapper;

    public PatientModel getPatientById(Long id) {
        return patientService.getPatientById(id);
    }

    public PatientModel createPatient(PatientModel patient) {
        return patientService.createPatient(patient);
    }

    public PatientModel updatePatient(Long id, PatientModel doctor) {
        return patientService.updatePatient(id, doctor);
    }

    public boolean deletePatient(Long id) {
        return patientService.deletePatient(id);
    }

    public PatientPage getAllPatients(PageRequestInput pageRequestInput,
                                PatientSpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage()-1, pageRequestInput.getSize(), pageRequestInput.getSort());

        if (specification == null) {
            specification = new PatientSpecificationDTO();
        }

        Page<PatientModel> doctors = patientService.getAllPatients(pageable, specification.getFullName(),
                specification.getName(),
                specification.getSurname(), specification.getDni(), specification.getGender(),
                specification.getMedicalSecureId());

        PatientPage patientPage = new PatientPage();
        patientPage.setPatients(doctors.getContent());
        patientPage.setPageInfo(pageInfoMapper.toPageInfo(doctors));
        return patientPage;

    }
}
