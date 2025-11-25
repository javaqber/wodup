package com.wodup_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Suscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación N:1 con Usuario (el atleta suscrito)
    @ManyToOne
    @JoinColumn(name = "atleta_id", nullable = false)
    private Usuario atleta;

    // Relación N:1 con Tarifa (la tarifa comprada)
    @ManyToOne
    @JoinColumn(name = "tarifa_id", nullable = false)
    private Tarifa tarifa;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fecha_incio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(name = "sesiones_restantes")
    private Integer sesiones_restantes; // si la tarifa es ilimitada

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "ACITVA"; // ACTIVA, VENCIDA, CANCELADA

}
