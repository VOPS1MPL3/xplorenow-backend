package com.xplorenow.calificacion;

import com.xplorenow.reserva.EstadoReserva;
import com.xplorenow.reserva.Reserva;
import com.xplorenow.reserva.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Endpoints de calificaciones (Punto 6 del TPO).
 *
 *   POST /reservas/{reservaId}/calificacion   -> Crear calificacion
 *   GET  /reservas/{reservaId}/calificacion   -> Ver mi calificacion
 *
 * Validaciones obligatorias:
 *   - Solo el dueno de la reserva puede calificarla
 *   - La reserva debe estar FINALIZADA
 *   - Maximo una calificacion por reserva
 *   - Solo dentro de las 48hs posteriores al horario
 *   - Ratings entre 1 y 5
 *   - Comentario maximo 300 caracteres
 */
@RestController
@RequestMapping("/reservas/{reservaId}/calificacion")
@RequiredArgsConstructor
public class CalificacionController {

    private static final long VENTANA_HORAS = 48;

    private final CalificacionRepository calificacionRepository;
    private final ReservaRepository reservaRepository;

    @PostMapping
    public ResponseEntity<?> crear(
            Authentication auth,
            @PathVariable Long reservaId,
            @RequestBody CrearCalificacionRequest req) {

        // Validaciones de los ratings
        if (req.getRatingActividad() == null || req.getRatingActividad() < 1 || req.getRatingActividad() > 5) {
            return error(400, "ratingActividad debe estar entre 1 y 5");
        }
        if (req.getRatingGuia() == null || req.getRatingGuia() < 1 || req.getRatingGuia() > 5) {
            return error(400, "ratingGuia debe estar entre 1 y 5");
        }
        if (req.getComentario() != null && req.getComentario().length() > 300) {
            return error(400, "El comentario no puede superar los 300 caracteres");
        }

        Reserva reserva = reservaRepository.findById(reservaId).orElse(null);
        if (reserva == null) {
            return error(404, "Reserva no encontrada");
        }

        // Solo el dueno puede calificar
        if (!reserva.getUsuario().getEmail().equals(auth.getName())) {
            return error(403, "No podes calificar reservas de otros usuarios");
        }

        // Solo se califican reservas FINALIZADAS
        if (reserva.getEstado() != EstadoReserva.FINALIZADA) {
            return error(400, "Solo se pueden calificar reservas FINALIZADAS");
        }

        // Una sola calificacion por reserva
        if (calificacionRepository.existsByReservaId(reservaId)) {
            return error(400, "Esta reserva ya tiene calificacion");
        }

        // Ventana de 48hs desde la fecha+hora del horario
        LocalDateTime finActividad = reserva.getHorario().getFecha()
                .atTime(reserva.getHorario().getHora());
        LocalDateTime limite = finActividad.plusHours(VENTANA_HORAS);
        if (LocalDateTime.now().isAfter(limite)) {
            return error(400, "Pasaron mas de 48hs desde la actividad, ya no se puede calificar");
        }

        Calificacion c = Calificacion.builder()
                .reserva(reserva)
                .ratingActividad(req.getRatingActividad())
                .ratingGuia(req.getRatingGuia())
                .comentario(req.getComentario())
                .build();
        Calificacion guardada = calificacionRepository.save(c);
        return ResponseEntity.ok(CalificacionDTO.desde(guardada));
    }

    @GetMapping
    public ResponseEntity<?> miCalificacion(
            Authentication auth,
            @PathVariable Long reservaId) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        if (!reserva.getUsuario().getEmail().equals(auth.getName())) {
            return error(403, "No podes ver calificaciones de otros usuarios");
        }

        return calificacionRepository.findByReservaId(reservaId)
                .map(CalificacionDTO::desde)
                .map(dto -> (ResponseEntity<?>) ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    private static ResponseEntity<?> error(int status, String msg) {
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }
}
