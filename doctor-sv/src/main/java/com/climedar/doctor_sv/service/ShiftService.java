package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.ShiftRepository;
import com.climedar.doctor_sv.specification.ShiftSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final DoctorService doctorService;

    public ShiftModel getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        return shiftMapper.toModel(shift);
    }

    public Page<ShiftModel> getAllShifts(Pageable pageable, ShiftSpecificationDTO shiftSpecificationDTO) {

        Specification<Shift> specification = getShiftSpecification(shiftSpecificationDTO);

        Page<Shift> shifts = shiftRepository.findAll(specification, pageable);

        return shifts.map(shiftMapper::toModel);
    }

    public ShiftModel createShift(CreateShiftDTO shiftDTO) {
        Doctor doctor = doctorService.getDoctorEntityById(shiftDTO.getDoctorId());

        Shift shift = switch (shiftDTO.getShiftCreateType()) {
            case SINGLE_PATIENT -> Shift.singlePatientShiftBuilder()
                                .date(shiftDTO.getDate())
                                .start(shiftDTO.getStartTime())
                                .place(shiftDTO.getPlace())
                                .doctor(doctor)
                                .build();

            case TIME_BASED -> Shift.startEndTimeBuilder()
                            .date(shiftDTO.getDate())
                            .time(shiftDTO.getStartTime(), shiftDTO.getEndTime())
                            .place(shiftDTO.getPlace())
                            .doctor(doctor)
                            .build();

            case PATIENT_BASED -> Shift.startTimePatientBuilder()
                            .date(shiftDTO.getDate())
                            .time(shiftDTO.getStartTime())
                            .patients(shiftDTO.getPatients())
                            .place(shiftDTO.getPlace())
                            .doctor(doctor)
                            .build();

            default -> throw new IllegalStateException("Unexpected value: " + shiftDTO.getShiftCreateType());
        };

        shiftRepository.save(shift);

        if (shiftDTO.getRecurringShift() != null) {
            List<Shift> recurringShifts = Shift.recurringShiftBuilder()
                    .startDate(shiftDTO.getRecurringShift().getStartDate())
                    .endDate(shiftDTO.getRecurringShift().getEndDate())
                    .validDays(shiftDTO.getRecurringShift().getValidDays())
                    .shiftTemplate(shift)
                    .build();
            shiftRepository.saveAll(recurringShifts);
        }

        return shiftMapper.toModel(shift);
    }

    public ShiftModel updateShift(Long id, ShiftModel shiftModel, ShiftSpecificationDTO shiftSpecificationDTO) {

        Specification<Shift> specification = getShiftSpecification(shiftSpecificationDTO);
        List<Shift> shifts = shiftRepository.findAll(specification);

        if (shifts.isEmpty()){
            throw new ClimedarException("NO_SHIFTS_FOUND", "No shifts found with the given criteria");
        }

        shifts.forEach(shift -> {
            shiftMapper.updateEntity(shift, shiftModel);
            shiftRepository.save(shift);
        });

        return shiftMapper.toModel(shifts.get(0));
    }

    public Boolean deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        shift.setDeleted(true);
        shiftRepository.save(shift);
        return true;
    }

    private static Specification<Shift> getShiftSpecification(ShiftSpecificationDTO shiftSpecificationDTO) {
        return Specification.where(ShiftSpecification.byDeleted(false))
                .and(ShiftSpecification.byDate(shiftSpecificationDTO.getDate(), shiftSpecificationDTO.getFromDate(), shiftSpecificationDTO.getToDate()))
                .and(ShiftSpecification.byStartTime(shiftSpecificationDTO.getStartTime()))
                .and(ShiftSpecification.byEndTime(shiftSpecificationDTO.getEndTime()))
                .and(ShiftSpecification.byPatients(shiftSpecificationDTO.getPatients()))
                .and(ShiftSpecification.byPlace(shiftSpecificationDTO.getPlace()))
                .and(ShiftSpecification.byState(shiftSpecificationDTO.getState()))
                .and(ShiftSpecification.byDoctorId(shiftSpecificationDTO.getDoctorId()));
    }


    public Set<LocalDate> getDatesWithShifts(LocalDate fromDate, LocalDate toDate) {
        Specification<Shift> specification = Specification.where(ShiftSpecification.byDeleted(false))
                .and(ShiftSpecification.byDate(null, fromDate, toDate));

        return shiftRepository.findAll(specification).stream().map(Shift::getDate).collect(Collectors.toSet());
    }
}
