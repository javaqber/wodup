package com.wodup_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Excluimos las entidades relacionadas para evitar recursión en toString y
// hashCode/equals
@ToString(exclude = { "atleta", "clase" })
@EqualsAndHashCode(exclude = { "atleta", "clase" })
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna para la fecha de reserva
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva = LocalDateTime.now();

    // Nuevo campo para el estado (CONFIRMADA, CANCELADA, etc.)
    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE";

    // Relación N:1 con Usuario (el atleta que reserva)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atleta_id", nullable = false)
    private Usuario atleta;

    // Relación N:1 con Clase (la clase reservada)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clase_id", nullable = false)
    @JsonIgnore
    private Clase clase;

    // Constructor para crear una reserva de forma sencilla
    public Reserva(Usuario atleta, Clase clase) {
        this.atleta = atleta;
        this.clase = clase;
        this.fechaReserva = LocalDateTime.now();
        this.estado = "CONFIRMADA"; // Estado inicial al crear una reserva
    }
}