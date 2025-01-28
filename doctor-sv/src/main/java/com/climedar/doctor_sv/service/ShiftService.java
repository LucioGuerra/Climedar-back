package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;

    public ShiftModel getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        return shiftMapper.toModel(shift);
    }

    public Page<ShiftModel> getAllShifts(Pageable pageable, LocalDate date, LocalTime startTime,
                                         LocalTime endTime, Integer patients, String place,
                                         ShiftState state, Long doctorId) {

        Specification<Shift> specification = Specification.where(null);
        Page<Shift> shifts = shiftRepository.findAll(specification, pageable);
        return shifts.map(shiftMapper::toModel);
    }

    public ShiftModel createShift(ShiftModel shiftModel) {//todo: hace falta un builder? donde deberia ir?
        Shift shift = shiftMapper.toEntity(shiftModel);
        shiftRepository.save(shift);
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
