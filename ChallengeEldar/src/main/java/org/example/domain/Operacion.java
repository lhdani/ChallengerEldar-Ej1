package org.example.domain;

public class Operacion {

    private double monto;

    public Operacion(double monto) {
        this.monto = monto;
    }

    public boolean esValida() {
        return monto < 10000;
    }

    public double getMonto() {
        return monto;
    }
}
