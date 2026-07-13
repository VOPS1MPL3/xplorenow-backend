package com.xplorenow.notificacion;

import com.xplorenow.horario.HorarioDTO;
import com.xplorenow.horario.HorarioService;
import com.xplorenow.horario.ReprogramarHorarioRequest;
import com.xplorenow.reserva.Reserva;
import com.xplorenow.reserva.ReservaDetalleDTO;
import com.xplorenow.reserva.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Simula el backoffice de la operadora (Punto 12 del TPO: "aviso inmediato
 * si la actividad es reprogramada o cancelada por la operadora").
 *
 * El TPO no pide un modulo de administracion real ni un rol operador, asi
 * que estos endpoints solo requieren JWT valido (cualquier usuario logueado
 * puede probarlos desde Postman), sin chequeo de dueño de la reserva. Es
 * una simplificacion deliberada para poder disparar y testear las Novedades
 * sin construir un backoffice completo.
 *
 *   POST /operador/reservas/{id}/cancelar
 *   POST /operador/horarios/{id}/reprogramar
 */
@RestController
@RequestMapping("/operador")
@RequiredArgsConstructor
public class OperadorController {

    private final ReservaService reservaService;
    private final HorarioService horarioService;

    @PostMapping("/reservas/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            Reserva r = reservaService.cancelarPorOperador(id);
            return ResponseEntity.ok(ReservaDetalleDTO.desde(r));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/horarios/{id}/reprogramar")
    public ResponseEntity<?> reprogramarHorario(@PathVariable Long id, @RequestBody ReprogramarHorarioRequest req) {
        try {
            var h = horarioService.reprogramar(id, req.getFecha(), req.getHora());
            return ResponseEntity.ok(HorarioDTO.desde(h));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
