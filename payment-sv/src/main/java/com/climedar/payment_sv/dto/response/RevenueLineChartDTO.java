package com.climedar.payment_sv.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenueLineChartDTO (
        String date,
        BigDecimal value
){
}
