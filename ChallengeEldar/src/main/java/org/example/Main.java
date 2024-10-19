package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final String JDBC_URL = "jdbc:h2:~/test"; // Cambia el path si es necesario
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        crearTablas(); // Crear las tablas en la base de datos si no existen
        while (true) {
            System.out.println("===== Menú del Sistema de Tarjetas de Crédito =====");
            System.out.println("1. Registrar persona");
            System.out.println("2. Registrar tarjeta");
            System.out.println("3. Consultar tarjetas por DNI");
            System.out.println("4. Consultar tasas de todas las marcas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea

            try {
                switch (opcion) {
                    case 1:
                        registrarPersona();
                        break;
                    case 2:
                        registrarTarjeta();
                        break;
                    case 3:
                        consultarTarjetasPorDni();
                        break;
                    case 4:
                        consultarTasas();
                        break;
                    case 5:
                        System.out.println("Saliendo del sistema...");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Crear las tablas en H2
    private static void crearTablas() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createUsuarioTable = "CREATE TABLE IF NOT EXISTS Usuario (" +
                    "dni VARCHAR(15) PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "apellido VARCHAR(100), " +
                    "fecha_nacimiento DATE, " +
                    "email VARCHAR(100))";

            String createTarjetaTable = "CREATE TABLE IF NOT EXISTS Tarjeta (" +
                    "numero_tarjeta VARCHAR(20) PRIMARY KEY, " +
                    "marca VARCHAR(10), " +
                    "fecha_vencimiento DATE, " +
                    "dni_titular VARCHAR(15), " +
                    "FOREIGN KEY (dni_titular) REFERENCES Usuario(dni))";

            stmt.execute(createUsuarioTable);
            stmt.execute(createTarjetaTable);

        } catch (SQLException e) {
            System.out.println("Error creando las tablas: " + e.getMessage());
        }
    }

    // Registrar una persona en la base de datos
    private static void registrarPersona() {
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el apellido: ");
        String apellido = scanner.nextLine();

        System.out.println("Ingrese el DNI: ");
        String dni = scanner.nextLine();

        System.out.println("Ingrese la fecha de nacimiento (yyyy-MM-dd): ");
        String fechaNacimientoStr = scanner.nextLine();
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.println("Ingrese el email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO Usuario (dni, nombre, apellido, fecha_nacimiento, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setDate(4, Date.valueOf(fechaNacimiento));
            pstmt.setString(5, email);
            pstmt.executeUpdate();

            System.out.println("Persona registrada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar persona: " + e.getMessage());
        }
    }

    // Registrar una tarjeta
    private static void registrarTarjeta() {
        System.out.println("Ingrese la marca de la tarjeta (VISA, NARA, AMEX): ");
        String marca = scanner.nextLine();

        System.out.println("Ingrese el número de la tarjeta: ");
        String numeroTarjeta = scanner.nextLine();

        System.out.println("Ingrese el DNI del titular: ");
        String dniTitular = scanner.nextLine();

        System.out.println("Ingrese la fecha de vencimiento (yyyy-MM-dd): ");
        String fechaVencimientoStr = scanner.nextLine();
        LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr, DateTimeFormatter.ISO_LOCAL_DATE);

        String sql = "INSERT INTO Tarjeta (numero_tarjeta, marca, fecha_vencimiento, dni_titular) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroTarjeta);
            pstmt.setString(2, marca);
            pstmt.setDate(3, Date.valueOf(fechaVencimiento));
            pstmt.setString(4, dniTitular);
            pstmt.executeUpdate();

            System.out.println("Tarjeta registrada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar tarjeta: " + e.getMessage());
        }
    }

    // Consultar tarjetas por DNI
    private static void consultarTarjetasPorDni() {
        System.out.println("Ingrese el DNI del usuario: ");
        String dni = scanner.nextLine();

        String sql = "SELECT t.numero_tarjeta, t.marca, t.fecha_vencimiento " +
                "FROM Tarjeta t " +
                "JOIN Usuario u ON t.dni_titular = u.dni " +
                "WHERE u.dni = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String numeroTarjeta = rs.getString("numero_tarjeta");
                String marca = rs.getString("marca");
                LocalDate fechaVencimiento = rs.getDate("fecha_vencimiento").toLocalDate();

                System.out.printf("Tarjeta: %s | Marca: %s | Fecha de Vencimiento: %s\n", numeroTarjeta, marca, fechaVencimiento);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar tarjetas: " + e.getMessage());
        }
    }

    // Consultar tasas de todas las marcas
    private static void consultarTasas() {
        System.out.println("Ingrese la fecha (dd-MM-yyyy) o presione Enter para usar la fecha actual: ");
        String fechaStr = scanner.nextLine();

        LocalDate fecha;
        if (fechaStr.isEmpty()) {
            fecha = LocalDate.now();
        } else {
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        System.out.println("Tasa VISA: " + calcularTasaVisa(fecha));
        System.out.println("Tasa NARA: " + calcularTasaNara(fecha));
        System.out.println("Tasa AMEX: " + calcularTasaAmex(fecha));
    }

    // Calcular tasa para VISA
    private static double calcularTasaVisa(LocalDate fecha) {
        return fecha.getYear() / (double) fecha.getMonthValue();
    }

    // Calcular tasa para NARA
    private static double calcularTasaNara(LocalDate fecha) {
        return fecha.getDayOfMonth() * 0.5;
    }

    // Calcular tasa para AMEX
    private static double calcularTasaAmex(LocalDate fecha) {
        return fecha.getMonthValue() * 0.1;
    }
}