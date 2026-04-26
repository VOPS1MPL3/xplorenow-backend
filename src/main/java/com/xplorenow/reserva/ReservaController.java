package com.xplorenow.reserva;

import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Endpoints de reservas (Puntos 4 y 5 del TPO).
 * Todos requieren JWT - el email del usuario sale de auth.getName().
 *
 *   POST /reservas                                         -> Crear
 *   GET  /reservas/mis?estado=CONFIRMADA                  -> Mis actividades
 *   GET  /reservas/{id}                                    -> Detalle / voucher
 *   POST /reservas/{id}/cancelar                          -> Cancelar
 *   GET  /reservas/historial?destinoId=&fechaDesde=&fechaHasta= -> Solo FINALIZADAS
 */
@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> crear(Authentication auth, @RequestBody CrearReservaRequest req) {
        try {
            Reserva r = reservaService.crear(auth.getName(), req);
            return ResponseEntity.ok(ReservaDetalleDTO.desde(r));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/mis")
    public ResponseEntity<List<ReservaDTO>> misReservas(
            Authentication auth,
            @RequestParam(required = false) EstadoReserva estado) {

        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<ReservaDTO> dtos = reservaRepository.misReservas(u, estado).stream()
                .map(ReservaDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDetalleDTO> detalle(Authentication auth, @PathVariable Long id) {
        return reservaRepository.findById(id)
                .filter(r -> r.getUsuario().getEmail().equals(auth.getName()))
                .map(ReservaDetalleDTO::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(Authentication auth, @PathVariable Long id) {
        try {
            Reserva r = reservaService.cancelar(auth.getName(), id);
            return ResponseEntity.ok(ReservaDetalleDTO.desde(r));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<ReservaDTO>> historial(
            Authentication auth,
            @RequestParam(required = false) Long destinoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<ReservaDTO> dtos = reservaRepository
                .historial(u, destinoId, fechaDesde, fechaHasta).stream()
                .map(ReservaDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
