package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.Payment;
import com.climedar.payment_sv.entity.Revenue;
import com.climedar.payment_sv.entity.RevenueType;
import com.climedar.payment_sv.event.internal.PaymentEvent;
import com.climedar.payment_sv.repository.RevenueRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RevenueService {

    private final RevenueRepository revenueRepository;
    private final PaymentService paymentService;


    @EventListener(condition = "#event.amount() > 0")
    public void handlerPaymentEvent(PaymentEvent event) {
        Revenue revenue = revenueRepository.findByDateAndRevenueType(event.date().toLocalDate(), RevenueType.DAILY).orElseThrow(() -> new RuntimeException("Daily revenue not found"));
        revenue.setAmount(revenue.getAmount().add(event.amount()));
        revenue.setTotalPayments(revenue.getTotalPayments() + 1);
        revenueRepository.save(revenue);
    }

    @EventListener(condition = "#event.amount() < 0")
    public void handlerCancelPaymentEvent(PaymentEvent event) {
        Revenue dailyRevenue = revenueRepository.findByDateAndRevenueType(event.date().toLocalDate(), RevenueType.DAILY).orElseThrow(() -> new RuntimeException("Daily revenue not found"));
        Revenue monthlyRevenue = revenueRepository.findByDateAndRevenueType(event.date().toLocalDate(), RevenueType.DAILY).orElseThrow(() -> new RuntimeException("Daily revenue not found"));
        monthlyRevenue.setAmount(monthlyRevenue.getAmount().add(event.amount()));
        dailyRevenue.setAmount(dailyRevenue.getAmount().add(event.amount()));
        revenueRepository.save(monthlyRevenue);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyAmount(){
        Revenue revenue = new Revenue();
        revenue.setDate(LocalDate.now());
        revenue.setAmount(BigDecimal.ZERO);
        revenue.setTotalPayments(0L);
        revenue.setRevenueType(RevenueType.DAILY);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void createMonthlyAmount(){
        Revenue revenue = new Revenue();
        revenue.setDate(LocalDate.now());
        revenue.setAmount(BigDecimal.ZERO);
        revenue.setTotalPayments(0L);
        revenue.setRevenueType(RevenueType.MONTHLY);
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void AddMonthlyAmount(){
        List<Payment> payments = paymentService.getPaymentsByDate(LocalDate.now());
        Revenue revenue = revenueRepository.findByDateAndRevenueType(LocalDate.now().withDayOfMonth(1), RevenueType.MONTHLY).orElseThrow(() -> new RuntimeException("Monthly revenue not found"));
        for (Payment payment : payments){
            revenue.setAmount(revenue.getAmount().add(payment.getAmount()));
            revenue.setTotalPayments(revenue.getTotalPayments() + 1);
        }
        revenueRepository.save(revenue);
    }

    @PostConstruct
    public void createInitialAmount(){
        Optional<Revenue> dailyRevenue = revenueRepository.findByDateAndRevenueType(LocalDate.now(), RevenueType.DAILY);
        Optional<Revenue> monthlyRevenue = revenueRepository.findByDateAndRevenueType(LocalDate.now().withDayOfMonth(1), RevenueType.MONTHLY);
        if (dailyRevenue.isEmpty()){
            createDailyAmount();
        }
        if (monthlyRevenue.isEmpty()){
            createMonthlyAmount();
        }
    }
}
