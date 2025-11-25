package com.wodup_backend.repository;

import com.wodup_backend.model.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    // Buscar la suscripción ACTIVA y que NO esté vencida del atleta
    Optional<Suscripcion> findByAtletaIdAndEstadoAndFechaFinGreaterThanEqual(
            Long atletaId, String estado, LocalDate fecha);
}