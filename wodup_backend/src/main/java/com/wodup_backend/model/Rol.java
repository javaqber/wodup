package com.wodup_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre; // ROLE_ADMIN, ROLE_COACH, ROLE_ATHLETE
}
