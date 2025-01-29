package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.ShiftRepository;
import com.climedar.doctor_sv.specification.ShiftSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    public Page<ShiftModel> getAllShifts(Pageable pageable, LocalDate date, LocalDate fromDate, LocalDate toDate,
                                         LocalTime startTime, LocalTime endTime, Integer patients, String place,
                                         ShiftState state, Long doctorId) {

        Specification<Shift> specification = Specification.where(ShiftSpecification.byDeleted(false))
                .and(ShiftSpecification.byDate(date, fromDate, toDate))
                .and(ShiftSpecification.byStartTime(startTime))
                .and(ShiftSpecification.byEndTime(endTime))
                .and(ShiftSpecification.byPatients(patients))
                .and(ShiftSpecification.byPlace(place))
                .and(ShiftSpecification.byState(state))
                .and(ShiftSpecification.byDoctorId(doctorId));

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

    public ShiftModel updateShift(Long id, ShiftModel shiftModel) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        shiftMapper.updateEntity(shift, shiftModel);
        shiftRepository.save(shift);
        return shiftMapper.toModel(shift);
    }

    public Boolean deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        shift.setDeleted(true);
        shiftRepository.save(shift);
        return true;
    }
}
