package com.climedar.consultation_sv.repository.feign.fallback;

import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.repository.feign.ShiftRepository;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class FallBackFactory  implements FallbackFactory<ShiftRepository> {

    @Override
    public ShiftRepository create(Throwable cause) {
        return new ShiftRepository() {
            @Override
            public Optional<Shift> findById(Long id) {
                if (cause instanceof FeignException.NotFound) {
                    return Optional.empty();
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new RuntimeException("Unhandled exception in Feign client", cause);
            }

            @Override
            public Page<Shift> getAllShift(LocalDate date, LocalTime starTime, LocalTime fromTime, LocalTime toTime) {
                return new PageImpl<>(new ArrayList<>());
            }

            @Override
            public List<Shift> findAllById(Set<Long> ids) {
                return List.of();
            }

            @Override
            public void occupyShift(Long id) {

            }

            @Override
            public void clearShift(Long id) {

            }

            @Override
            public Shift createShift(Long doctorId, Duration timeOfShifts) {
                return null;
            }

            @Override
            public Doctor findDoctorById(Long id) {
                return null;
            }
        };
    }
}
