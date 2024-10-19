package org.example.domain;

import java.time.LocalDate;

public enum MarcaTarjeta {

    VISA, NARA, AMEX;

    public static double calcularTasa(String marca) {
        LocalDate today = LocalDate.now();
        switch (marca.toUpperCase()) {
            case "VISA":
                return today.getYear() / (double) today.getMonthValue();
            case "NARA":
                return today.getDayOfMonth() * 0.5;
            case "AMEX":
                return today.getMonthValue() * 0.1;
            default:
                throw new IllegalArgumentException("Marca de tarjeta no soportada");
        }
    }
}
