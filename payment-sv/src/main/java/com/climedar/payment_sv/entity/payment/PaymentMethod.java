package com.climedar.payment_sv.entity.payment;

public enum PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    CUENTA_DNI,
    MERCADO_PAGO,
    TRANSFER;

    public String getDisplayName() {
        return switch (this) {
            case CASH -> "Efectivo";
            case CREDIT_CARD -> "Tarjeta de crédito";
            case DEBIT_CARD -> "Tarjeta de débito";
            case PAYPAL -> "Paypal";
            case CUENTA_DNI -> "Cuenta DNI";
            case MERCADO_PAGO -> "Mercado Pago";
            case TRANSFER -> "Transferencia";
        };
    }
}

