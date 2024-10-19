package org.example.domain;

import java.time.LocalDate;

public class Tarjeta {

    private String marca;
    private String numeroTarjeta;
    private Usuario cardholder;
    private LocalDate fechaVencimiento;
    private String cvv;

    public Tarjeta(String marca, String numeroTarjeta, Usuario cardholder, LocalDate fechaVencimiento, String cvv) {
        this.marca = marca;
        this.numeroTarjeta = numeroTarjeta;
        this.cardholder = cardholder;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
    }

    public String getMarca() {
        return marca;
    }

    public boolean esValida() {
        return fechaVencimiento.isAfter(LocalDate.now());
    }

    public boolean validarCVV(String cvvIngresado) {
        return this.cvv.equals(cvvIngresado);
    }

    public double calcularTasa() {
        return MarcaTarjeta.calcularTasa(marca);
    }
}
