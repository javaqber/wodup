package com.wodup_backend.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Tarifa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre; // MENSUAL, ILIMITADA, 10 SESIONES

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio; // MENSUAL, ILIMITADA, 10 SESIONES

    @Column(name = "duracion_dias", nullable = false)
    private Integer duracionDias; // 30, 90, etc..

    // Al ser NULL en BD, usamos Integer y no int.
    @Column(name = "limite_sesiones", nullable = true)
    private Integer limiteSesiones; // NULL si es ilimitada

}
