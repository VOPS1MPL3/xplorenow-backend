package com.xplorenow.notificacion;

import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Punto 12 del TPO: "Recordatorios y Avisos (Notificaciones Push)".
 *
 * No hay push real (FCM/APNs) en el alcance del TPO -> se implementa via
 * long polling, tal cual el approach didactico visto en la ultima clase:
 * el cliente pide una vez, el servidor sostiene la conexion chequeando
 * periodicamente, y responde apenas hay novedades (o vacio al timeout).
 *
 *   GET /notificaciones/novedades?ultimaFecha=2026-07-13T10:00:00
 *
 * El cliente debe guardar la "fecha" de la ultima Novedad recibida y
 * mandarla como ultimaFecha en el proximo pedido (cursor). Si no manda
 * nada (primera vez), se toma "ahora" para no traer historial viejo.
 */
@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NovedadRepository novedadRepository;
    private final UsuarioRepository usuarioRepository;
    private final NovedadService novedadService;

    /** Cuanto tiempo sostiene la conexion como maximo antes de responder vacio (204). */
    private static final long TIMEOUT_MS = 25_000;
    /** Cada cuanto re-chequea la base mientras sostiene la conexion. */
    private static final long INTERVALO_MS = 1_000;

    @GetMapping("/novedades")
    public ResponseEntity<?> novedades(
            Authentication auth,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ultimaFecha) {

        Usuario usuario;
        try {
            usuario = usuarioRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }

        LocalDateTime desde = (ultimaFecha != null) ? ultimaFecha : LocalDateTime.now();

        long inicio = System.currentTimeMillis();
        while (System.currentTimeMillis() - inicio < TIMEOUT_MS) {
            List<Novedad> novedades = novedadRepository
                    .findByUsuarioAndFechaAfterOrderByFechaAsc(usuario, desde);

            if (!novedades.isEmpty()) {
                List<NovedadDTO> dtos = novedades.stream().map(NovedadDTO::desde).toList();
                novedadService.marcarLeidas(novedades);
                return ResponseEntity.ok(dtos);
            }

            try {
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.noContent().build();
            }
        }

        // Timeout sin novedades: el cliente debe volver a pedir enseguida
        return ResponseEntity.noContent().build();
    }

    /**
     * Novedades generadas mientras la app estaba cerrada. El long polling
     * solo entrega lo que pasa a partir del momento de conexion, asi que el
     * cliente llama a esto una vez al arrancar la sesion para no perderse
     * nada (tipicamente el recordatorio de 24hs, que lo genera un job).
     *
     *   GET /notificaciones/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<?> pendientes(Authentication auth) {
        Usuario usuario;
        try {
            usuario = usuarioRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }

        List<NovedadDTO> dtos = novedadService.obtenerPendientesYMarcarLeidas(usuario)
                .stream().map(NovedadDTO::desde).toList();

        return ResponseEntity.ok(dtos);
    }
}
