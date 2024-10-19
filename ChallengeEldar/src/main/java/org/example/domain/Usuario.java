package org.example.domain;

import java.time.LocalDate;

public class Usuario {

    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private String email;

    public Usuario(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
