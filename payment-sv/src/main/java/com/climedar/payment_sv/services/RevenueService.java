package com.climedar.payment_sv.services;

import com.climedar.library.exception.ClimedarException;
import com.climedar.payment_sv.dto.request.RevenueSpecificationDTO;
import com.climedar.payment_sv.dto.response.RevenueLineChartDTO;
import com.climedar.payment_sv.dto.response.RevenuePieChartDTO;
import com.climedar.payment_sv.entity.revenue.Revenue;
import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.event.PaymentEvent;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import com.climedar.payment_sv.mapper.RevenueMapper;
import com.climedar.payment_sv.repository.MedicalServicesRepository;
import com.climedar.payment_sv.repository.RevenueRepository;
import com.climedar.payment_sv.repository.SpecialityRepository;
import com.climedar.payment_sv.specification.RevenueSpecification;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final MedicalServicesRepository medicalServicesRepository;
    private final SpecialityRepository specialityRepository;
    private final RevenueMapper revenueMapper;

    //todo: actualizar los datos cuando se modifiquen las especialidades

    public List<RevenuePieChartDTO> getAllRevenuePieChart(RevenueSpecificationDTO specificationDTO) {

        if (specificationDTO == null) {
            specificationDTO = new RevenueSpecificationDTO();
        }

        Specification<Revenue> specification =
                Specification.where(RevenueSpecification.byDate(specificationDTO.getDate(), specificationDTO.getFromDate(), specificationDTO.getToDate()))
                .and(RevenueSpecification.byType(specificationDTO.getRevenueType()))
                .and(RevenueSpecification.bySpeciality(specificationDTO.getSpecialityName()))
                .and(RevenueSpecification.byMedicalService(specificationDTO.getServiceType()));

        List<Revenue> revenues = revenueRepository.findAll(specification);

         return switch (specificationDTO.getOriginName()) {
            case SPECIALITY -> revenues.stream().map(revenueMapper::toDTOSpecialityName).toList();
            case MEDICAL_SERVICE -> revenues.stream().map(revenueMapper::toDTOServiceType).toList();
         };
    }

    public List<RevenueLineChartDTO> getAllRevenueLineChart(String fromDate, String toDate, RevenueType revenueType) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        if (from.isAfter(to)) {
            throw new ClimedarException("DATE_FROM_IS_AFTER_DATE_TO", "The date from is after the date to");
        }

        List<RevenueLineChartDTO> revenues = new ArrayList<>();


        List<Object[]> result = revenueRepository.findByDateBetweenAndRevenueType(from, to, revenueType);
        Map<LocalDate, BigDecimal> revenueMap = result.stream()
                .collect(Collectors.toMap(
                        row -> (LocalDate) row[0],
                        row -> (BigDecimal) row[1]
                ));

        LocalDate current;
        switch (revenueType){
            case DAILY -> {
                current = from;
                while (current.isBefore(to)){
                    BigDecimal totalToCurrent = revenueMap.getOrDefault(current, BigDecimal.ZERO);
                    RevenueLineChartDTO revenueLineChartDTO = new RevenueLineChartDTO(current, totalToCurrent);
                    revenues.add(revenueLineChartDTO);
                    current = current.plusDays(1);
                }
            }
            case MONTHLY -> {
                current = from.withDayOfMonth(1);
                while (current.isBefore(to)){
                    BigDecimal totalToCurrent = revenueMap.getOrDefault(current, BigDecimal.ZERO);
                    RevenueLineChartDTO revenueLineChartDTO = new RevenueLineChartDTO(current, totalToCurrent);
                    revenues.add(revenueLineChartDTO);
                    current = current.plusMonths(1);
                }
            }
        }

        return revenues;
    }

    @EventListener(condition = "#event.amount() > 0")
    public void handlerPaymentEvent(PaymentEvent event) {
        Revenue revenue =
                revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServiceType((event.date().toLocalDate()),
                RevenueType.DAILY, event.specialityName(), event.serviceType());
        revenue.setAmount(revenue.getAmount().add(event.amount()));
        revenue.setTotalPayments(revenue.getTotalPayments() + 1);
        revenueRepository.save(revenue);
    }

    @EventListener(condition = "#event.amount() < 0")
    public void handlerCancelPaymentEvent(PaymentEvent event) {
        Revenue dailyRevenue = revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServiceType(event.date().toLocalDate(),
                RevenueType.DAILY, event.specialityName(), event.serviceType());
        Revenue monthlyRevenue = revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServiceType(event.date().toLocalDate(),
                RevenueType.DAILY, event.specialityName(), event.serviceType());
        monthlyRevenue.setAmount(monthlyRevenue.getAmount().add(event.amount()));
        dailyRevenue.setAmount(dailyRevenue.getAmount().add(event.amount()));
        revenueRepository.save(monthlyRevenue);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyAmount(){
        Set<String> specialitiesNames = specialityRepository.getAllSpecialitiesName();
        Set<ServiceType> serviceTypes = medicalServicesRepository.getAllServicesType();
        for (String specialityName : specialitiesNames){
            for (ServiceType serviceType : serviceTypes){
                Revenue revenue = new Revenue();
                revenue.setDate(LocalDate.now());
                revenue.setAmount(BigDecimal.ZERO);
                revenue.setTotalPayments(0L);
                revenue.setSpecialityName(specialityName);
                revenue.setMedicalServiceType(serviceType);
                revenue.setRevenueType(RevenueType.DAILY);
                revenueRepository.save(revenue);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void createMonthlyAmount(){
        Set<String> specialitiesNames = specialityRepository.getAllSpecialitiesName();
        Set<ServiceType> serviceTypes = medicalServicesRepository.getAllServicesType();
        for (String specialityName : specialitiesNames){
            for (ServiceType serviceType : serviceTypes){
                Revenue revenue = new Revenue();
                revenue.setDate(LocalDate.now().withDayOfMonth(1));
                revenue.setAmount(BigDecimal.ZERO);
                revenue.setTotalPayments(0L);
                revenue.setSpecialityName(specialityName);
                revenue.setMedicalServiceType(serviceType);
                revenue.setRevenueType(RevenueType.MONTHLY);
                revenueRepository.save(revenue);
            }
        }
    }


    @Scheduled(cron = "0 59 23 * * ?")
    public void AddMonthlyAmount(){
        List<Revenue> monthlyRevenues = revenueRepository.findByDateAndRevenueType(LocalDate.now().withDayOfMonth(1),
                RevenueType.MONTHLY);
        List<Revenue> dailyRevenues = revenueRepository.findByDateAndRevenueType(LocalDate.now(), RevenueType.DAILY);
        for (Revenue monthyRevenue : monthlyRevenues){
            for (Revenue dailyRevenue : dailyRevenues){
                if (monthyRevenue.getSpecialityName().equals(dailyRevenue.getSpecialityName()) &&
                    monthyRevenue.getMedicalServiceType().equals(dailyRevenue.getMedicalServiceType())){
                    monthyRevenue.setAmount(monthyRevenue.getAmount().add(dailyRevenue.getAmount()));
                    monthyRevenue.setTotalPayments(monthyRevenue.getTotalPayments() + dailyRevenue.getTotalPayments());
                }
            }
        }
        revenueRepository.saveAll(monthlyRevenues);
    }

    @PostConstruct
    public void createInitialAmount(){
        try {
            Thread.sleep(10*1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        List<Revenue> dailyRevenues = revenueRepository.findByDateAndRevenueType(LocalDate.now(), RevenueType.DAILY);
        List<Revenue> monthlyRevenues = revenueRepository.findByDateAndRevenueType(LocalDate.now().withDayOfMonth(1),
                RevenueType.MONTHLY);
        if (dailyRevenues.isEmpty()){
            createDailyAmount();
        }
        if (monthlyRevenues.isEmpty()){
            createMonthlyAmount();
        }
    }
}
