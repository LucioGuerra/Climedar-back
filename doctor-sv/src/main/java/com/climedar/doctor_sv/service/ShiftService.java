package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.builder.shift.ShiftDirector;
import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.ShiftBuilder;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.entity.ShiftState;
import com.climedar.doctor_sv.external.event.published.ShiftCanceledEvent;
import com.climedar.doctor_sv.mapper.ShiftMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.repository.feign.ConsultationRepository;
import com.climedar.doctor_sv.repository.ShiftRepository;
import com.climedar.doctor_sv.specification.ShiftSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final DoctorService doctorService;
    private final ShiftDirector shiftDirector;
    private final ConsultationRepository consultationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ShiftModel getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        ShiftModel shiftModel = shiftMapper.toModel(shift);
        shiftModel.setDoctor(doctorService.getDoctorById(shift.getDoctor().getId()));
        return shiftModel;
    }

    public Page<ShiftModel> getAllShifts(Pageable pageable, ShiftSpecificationDTO shiftSpecificationDTO) {

        Specification<Shift> specification = getShiftSpecification(shiftSpecificationDTO);
        Page<Shift> shifts = shiftRepository.findAll(specification, pageable);

        Set<Doctor> doctors = shifts.stream()
                .map(Shift::getDoctor)
                .collect(Collectors.toSet());


        Map<Long, DoctorModel> doctorModelMap = doctorService.getDoctorsModelsFromDoctors(doctors);

        return shifts.map(shift -> {
            ShiftModel shiftModel = shiftMapper.toModel(shift);
            shiftModel.setDoctor(doctorModelMap.get(shift.getDoctor().getId()));
            return shiftModel;
        });
    }

    @Transactional
    public List<ShiftModel> createShift(CreateShiftDTO shiftDTO) {
        Doctor doctor = doctorService.getDoctorEntityById(shiftDTO.getDoctorId());
        List<Shift> shifts = new ArrayList<>();


        if (shiftDTO.getShiftBuilder().equals(ShiftBuilder.RECURRING)) {
            shifts = shiftDirector.constructRecurringMultipleShifts(shiftDTO, doctor);
        }
        if (shiftDTO.getShiftBuilder().equals(ShiftBuilder.REGULAR)) {
            shifts = shiftDirector.constructMultipleShifts(shiftDTO, doctor);
        }
        if (shiftDTO.getShiftBuilder().equals(ShiftBuilder.OVERTIME)) {
            shifts = List.of(shiftDirector.constructOvertimeShift(shiftDTO, doctor));
        }

        shiftRepository.saveAll(shifts);
        return shifts.stream().map(shiftMapper::toModel).toList();
    }


    @Transactional
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

    @Transactional
    public Boolean deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));

        if (consultationRepository.existConsultationForShift(id)){
            throw new ClimedarException("SHIFT_HAS_A_CONSULTATION", "Shift has a consultation and doesnt could be deleted");
        }

        shift.setDeleted(true);
        shiftRepository.save(shift);
        return true;
    }

    private static Specification<Shift> getShiftSpecification(ShiftSpecificationDTO shiftSpecificationDTO) {
        return Specification.where(ShiftSpecification.byDeleted(false))
                .and(ShiftSpecification.byDate(shiftSpecificationDTO.getDate(), shiftSpecificationDTO.getFromDate(), shiftSpecificationDTO.getToDate()))
                .and(ShiftSpecification.byTime(shiftSpecificationDTO.getFromTime(),
                                shiftSpecificationDTO.getToTime()))
                .and(ShiftSpecification.byStartTime(shiftSpecificationDTO.getStartTime()))
                .and(ShiftSpecification.byEndTime(shiftSpecificationDTO.getEndTime()))
                .and(ShiftSpecification.byPatients(shiftSpecificationDTO.getPatients()))
                .and(ShiftSpecification.byPlace(shiftSpecificationDTO.getPlace()))
                .and(ShiftSpecification.byState(shiftSpecificationDTO.getState()))
                .and(ShiftSpecification.byDoctorId(shiftSpecificationDTO.getDoctorId()));
    }


    public Set<LocalDate> getDatesWithShifts(String fromDate, String toDate, Long doctorId) {
        Specification<Shift> specification = Specification.where(ShiftSpecification.byDeleted(false))
                .and(ShiftSpecification.byDate(null, fromDate, toDate))
                .and(ShiftSpecification.byDoctorId(doctorId));

        return shiftRepository.findAll(specification).stream()
                .map(Shift::getDate)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    public List<ShiftModel> findAllById(Set<Long> ids) {
        List<Shift> shifts = shiftRepository.findAllByIdAndNotDeleted(ids);
        return shifts.stream().map((shift)->{
            ShiftModel shiftModel = shiftMapper.toModel(shift);
            shiftModel.setDoctor(doctorService.getDoctorById(shift.getDoctor().getId()));
            return shiftModel;
                }).toList();
    }

    public void occupyShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        if (shift.getState() == ShiftState.OCCUPIED) {
            throw new ClimedarException("SHIFT_AlREADY_OCCUPIED", "Shift is already occupied");
        }
        if (shift.getState() == ShiftState.CANCELED) {
            throw new ClimedarException("SHIFT_WAS_CANCELED", "Shift is already canceled");
        }
        shift.setState(ShiftState.OCCUPIED);
        shiftRepository.save(shift);
    }

    public void clearShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        if (shift.getState() == ShiftState.AVAILABLE) {
            throw new ClimedarException("SHIFT_AlREADY_OCCUPIED", "Shift is already occupied");
        }
        shift.setState(ShiftState.AVAILABLE);
        shiftRepository.save(shift);
    }

    @Transactional //TODO: Debe permitir cancelar muchos turnos
    public ShiftModel cancelShift(Long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        if (shift.getState() == ShiftState.CANCELED) {
            return shiftMapper.toModel(shift);
        }
        shift.setState(ShiftState.CANCELED);
        shiftRepository.save(shift);
        //TODO: erase consultation and send notification to the patient
        kafkaTemplate.send("shift-canceled", new ShiftCanceledEvent(shift.getId()));
        return shiftMapper.toModel(shift);
    }
}
