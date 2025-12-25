package com.wodup_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO (Data Transfer Object) utilizado para exponer los datos de Clase
 * de forma pública, evitando problemas de serialización de Hibernate
 * y la exposición de datos internos como el objeto Usuario completo (Coach).
 */
public class ClaseDTO {
    private Long id;
    private String nombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int capacidad;
    // Opcionalmente, puedes incluir solo el ID del coach o el nombre.
    private Long coachId;

    // Constructor vacío (necesario para algunas operaciones de Spring)
    public ClaseDTO() {
    }

    // Constructor para mapear desde el modelo Clase
    public ClaseDTO(Long id, String nombre, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin, int capacidad, Long coachId) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.capacidad = capacidad;
        this.coachId = coachId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }
}