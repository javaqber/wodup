package com.wodup_backend.repository;

import com.wodup_backend.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
    // Método útil para el calendario, que busca clases en o después de una fecha
    List<Clase> findByFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(LocalDate fecha);

    // Método para asegurar que solo se crea una clase por día
    boolean existsByFecha(LocalDate fecha);

    // Obtiene la clase del dia actual
    Optional<Clase> findByFecha(LocalDate fecha);
}
