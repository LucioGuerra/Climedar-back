package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Revenue;
import com.climedar.payment_sv.entity.RevenueType;
import com.climedar.payment_sv.event.internal.PaymentEvent;
import com.climedar.payment_sv.external.model.medical_services.ServicesType;
import com.climedar.payment_sv.repository.MedicalServicesRepository;
import com.climedar.payment_sv.repository.RevenueRepository;
import com.climedar.payment_sv.repository.SpecialityRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final MedicalServicesRepository medicalServicesRepository;
    private final SpecialityRepository specialityRepository;

    //todo: obtener datos de las ganancias
    //todo: actualizar los datos cuando se modifiquen las especialidades

    @EventListener(condition = "#event.amount() > 0")
    public void handlerPaymentEvent(PaymentEvent event) {
        Revenue revenue =
                revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServicesType((event.date().toLocalDate()),
                RevenueType.DAILY, event.specialityName(), event.servicesType());
        revenue.setAmount(revenue.getAmount().add(event.amount()));
        revenue.setTotalPayments(revenue.getTotalPayments() + 1);
        revenueRepository.save(revenue);
    }

    @EventListener(condition = "#event.amount() < 0")
    public void handlerCancelPaymentEvent(PaymentEvent event) {
        Revenue dailyRevenue = revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServicesType(event.date().toLocalDate(),
                RevenueType.DAILY, event.specialityName(), event.servicesType());
        Revenue monthlyRevenue = revenueRepository.findByDateAndRevenueTypeAndSpecialityNameAndMedicalServicesType(event.date().toLocalDate(),
                RevenueType.DAILY, event.specialityName(), event.servicesType());
        monthlyRevenue.setAmount(monthlyRevenue.getAmount().add(event.amount()));
        dailyRevenue.setAmount(dailyRevenue.getAmount().add(event.amount()));
        revenueRepository.save(monthlyRevenue);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyAmount(){
        Set<String> specialitiesNames = specialityRepository.getAllSpecialitiesName();
        Set<ServicesType> servicesTypes = medicalServicesRepository.getAllServicesType();
        for (String specialityName : specialitiesNames){
            for (ServicesType servicesType : servicesTypes){
                Revenue revenue = new Revenue();
                revenue.setDate(LocalDate.now());
                revenue.setAmount(BigDecimal.ZERO);
                revenue.setTotalPayments(0L);
                revenue.setSpecialityName(specialityName);
                revenue.setMedicalServicesType(servicesType);
                revenue.setRevenueType(RevenueType.DAILY);
                revenueRepository.save(revenue);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void createMonthlyAmount(){
        Set<String> specialitiesNames = specialityRepository.getAllSpecialitiesName();
        Set<ServicesType> servicesTypes = medicalServicesRepository.getAllServicesType();
        for (String specialityName : specialitiesNames){
            for (ServicesType servicesType : servicesTypes){
                Revenue revenue = new Revenue();
                revenue.setDate(LocalDate.now().withDayOfMonth(1));
                revenue.setAmount(BigDecimal.ZERO);
                revenue.setTotalPayments(0L);
                revenue.setSpecialityName(specialityName);
                revenue.setMedicalServicesType(servicesType);
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
                    monthyRevenue.getMedicalServicesType().equals(dailyRevenue.getMedicalServicesType())){
                    monthyRevenue.setAmount(monthyRevenue.getAmount().add(dailyRevenue.getAmount()));
                    monthyRevenue.setTotalPayments(monthyRevenue.getTotalPayments() + dailyRevenue.getTotalPayments());
                }
            }
        }
        revenueRepository.saveAll(monthlyRevenues);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void createInitialAmount(){
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
