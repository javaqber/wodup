package com.wodup_backend.service;

import com.wodup_backend.model.*;
import com.wodup_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    // 1. Obtener las reservas de un atleta específico
    public List<Reserva> getReservasByAtletaId(Long atletaId) {
        return reservaRepository.findByAtletaId(atletaId);
    }

    /**
     * Realiza una nueva reserva para un atleta (identificado por email) en una
     * clase específica.
     * CORRECCIÓN DE LA FIRMA: Ahora acepta usuarioEmail (String) y claseId (Long).
     */
    @Transactional
    public Reserva crearReserva(String usuarioEmail, Long claseId) { // FIRMA CORRECTA

        // 1. VALIDACIÓN DE ENTIDADES
        // Buscamos al atleta por email (usamos el método del controlador)
        Usuario atleta = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Atleta no encontrado con email: " + usuarioEmail));

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada."));

        Long atletaId = atleta.getId(); // Obtenemos el ID del atleta para las consultas del repositorio

        // 2. VALIDACIÓN DE DISPONIBILIDAD DE CLASE
        long reservasActuales = reservaRepository.countByClaseIdAndEstado(claseId, "CONFIRMADA");
        if (reservasActuales >= clase.getCapacidad()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clase llena. No quedan plazas disponibles.");
        }

        // 3. VALIDACIÓN DE DUPLICIDAD
        if (reservaRepository.findByAtletaIdAndClaseId(atletaId, claseId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una reserva para esta clase.");
        }

        // 4. VALIDACIÓN DE SUSCRIPCIÓN Y SESIONES
        Suscripcion suscripcionActiva = suscripcionRepository
                .findByAtletaIdAndEstadoAndFechaFinGreaterThanEqual(atletaId, "ACTIVA", LocalDate.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "No tienes una suscripción ACTIVA o está VENCIDA."));

        // Si la tarifa tiene límite de sesiones y se acabaron
        if (suscripcionActiva.getTarifa().getLimiteSesiones() != null &&
                suscripcionActiva.getSesiones_restantes() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Límite de sesiones de la tarifa alcanzado.");
        }

        // 5. CREACIÓN DE LA RESERVA
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setAtleta(atleta);
        nuevaReserva.setClase(clase);
        nuevaReserva.setFechaReserva(LocalDateTime.now());
        nuevaReserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        // 6. ACTUALIZACIÓN DE SESIONES RESTANTES
        if (suscripcionActiva.getTarifa().getLimiteSesiones() != null) {
            suscripcionActiva.setSesiones_restantes(suscripcionActiva.getSesiones_restantes() - 1);
            suscripcionRepository.save(suscripcionActiva);
        }

        return reservaGuardada;
    }

    // CANCELA UNA RESERVA EXISTENTE
    @Transactional
    public void cancelarReserva(Long reservaId, Long atletaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada."));

        // Asegurarse de que el atleta solo pueda cancelar sus propias reservas
        if (!reserva.getAtleta().getId().equals(atletaId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para cancelar esta reserva.");
        }

        // 1. Validar tiempo límite para cancelar (Ejemplo: no se puede cancelar 1 hora
        // antes)
        if (reserva.getClase().getFecha().atTime(reserva.getClase().getHoraInicio()).minusHours(1)
                .isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede cancelar la reserva con menos de 1 hora de antelación.");
        }

        if (reserva.getEstado().equals("CANCELADA")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reserva ya está cancelada.");
        }

        // 2. CAMBIO DE ESTADO
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        // 3. Devolución de sesión (si aplica)
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository
                .findByAtletaIdAndEstadoAndFechaFinGreaterThanEqual(atletaId, "ACTIVA", LocalDate.now());

        suscripcionOpt.ifPresent(suscripcion -> {
            if (suscripcion.getTarifa().getLimiteSesiones() != null) {
                suscripcion.setSesiones_restantes(suscripcion.getSesiones_restantes() + 1);
                suscripcionRepository.save(suscripcion);
            }
        });
    }
}