package com.climedar.payment_sv.dto.response;

import java.math.BigDecimal;

public record RevenuePieChartDTO(
        String name,
        BigDecimal value
) {
}
