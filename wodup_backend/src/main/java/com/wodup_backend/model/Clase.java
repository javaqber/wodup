package com.wodup_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Importante: Excluimos 'reservas' para evitar recursión.
@ToString(exclude = { "reservas" })
@EqualsAndHashCode(exclude = { "reservas" })
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    // Relación N:1 con Usuario (el coach_id es la FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonIgnore
    private Usuario coach;

    // Relación 1:N con Reserva (una clase tiene muchas reservas)
    // Usamos mappedBy que apunta al campo 'clase' en la entidad Reserva.
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Reserva> reservas = new HashSet<>();
}