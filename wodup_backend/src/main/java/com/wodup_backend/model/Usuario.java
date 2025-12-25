package com.wodup_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "roles", "misReservas" })
@EqualsAndHashCode(exclude = { "roles", "misReservas" })
public class Usuario {

    // Constructor necesario para crear el Usuario en AuthService
    public Usuario(String nombre, String apellido, String email, String password, LocalDate fechaRegistro,
            boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 255, nullable = false)
    private String apellido;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    // Relación N:M con Rol (Tabla intermedia Usuario_rol)
    @ManyToMany(fetch = FetchType.EAGER) // Carga los roles al buscar el usuario
    @JoinTable(name = "Usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    @JsonIgnore
    private Set<Rol> roles = new HashSet<>();

    // Relación 1:N con Reserva (un usuario tiene muchas reservas)
    // El 'mappedBy' apunta al campo 'atleta' en la entidad Reserva,
    // creando una relación bidireccional basada en la entidad de unión Reserva.
    @OneToMany(mappedBy = "atleta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Reserva> misReservas = new HashSet<>();
}