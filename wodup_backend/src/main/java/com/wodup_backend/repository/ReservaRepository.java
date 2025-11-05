package com.wodup_backend.repository;

import com.wodup_backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar todas las reservas de un atleta
    List<Reserva> findByAtletaId(Long atletaId);

    // Contar reservas en una clase específica (para capacidad)
    long countByClaseIdAndEstado(Long claseId, String estado);

    // Verificar si ya existe una reserva para esa clase por ese atleta
    Optional<Reserva> findByAtletaIdAndClaseId(Long atletaId, Long claseId);
}
