package com.climedar.payment_sv.dto.response;

import java.math.BigDecimal;

public record GetRevenueDTO(
        String name,
        BigDecimal amount
) {
}
