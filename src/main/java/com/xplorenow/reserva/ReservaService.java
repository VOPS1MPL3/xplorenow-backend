package com.xplorenow.reserva;

import com.xplorenow.horario.Horario;
import com.xplorenow.horario.HorarioRepository;
import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Toda la logica de negocio de reservas:
 *  - Crear reserva validando cupos en tiempo real
 *  - Cancelar reserva devolviendo cupos al horario
 *  - Job programado que marca FINALIZADAS las reservas con horario pasado
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea una reserva atomicamente:
     *  1. Valida que el horario tenga cupos suficientes
     *  2. Decrementa cupos
     *  3. Genera voucher unico
     *  4. Persiste la reserva
     *
     * @Transactional asegura que si falla algo, no quede el horario con
     * cupos decrementados sin reserva creada (rollback automatico).
     */
    @Transactional
    public Reserva crear(String email, CrearReservaRequest req) {
        if (req.getCantidadParticipantes() == null || req.getCantidadParticipantes() < 1) {
            throw new IllegalArgumentException("La cantidad de participantes debe ser >= 1");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Horario horario = horarioRepository.findById(req.getHorarioId())
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        if (horario.getCuposRestantes() < req.getCantidadParticipantes()) {
            throw new IllegalStateException("No hay cupos suficientes (disponibles: "
                    + horario.getCuposRestantes() + ")");
        }

        // Decrementar cupos
        horario.setCuposRestantes(horario.getCuposRestantes() - req.getCantidadParticipantes());
        horarioRepository.save(horario);

        // Crear reserva
        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .actividad(horario.getActividad())
                .horario(horario)
                .cantidadParticipantes(req.getCantidadParticipantes())
                .estado(EstadoReserva.CONFIRMADA)
                .voucherCodigo(generarVoucher())
                .build();

        return reservaRepository.save(reserva);
    }

    /**
     * Cancela una reserva. Solo el dueno puede cancelar la suya.
     * Devuelve los cupos al horario.
     */
    @Transactional
    public Reserva cancelar(String email, Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        if (!reserva.getUsuario().getEmail().equals(email)) {
            throw new SecurityException("No podes cancelar reservas de otros usuarios");
        }

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo se pueden cancelar reservas CONFIRMADAS");
        }

        // Devolver cupos
        Horario h = reserva.getHorario();
        h.setCuposRestantes(h.getCuposRestantes() + reserva.getCantidadParticipantes());
        horarioRepository.save(h);

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setCanceladaEn(LocalDateTime.now());
        return reservaRepository.save(reserva);
    }

    /**
     * Job programado: cada 5 minutos marca como FINALIZADA toda reserva
     * cuyo horario ya paso y sigue en estado CONFIRMADA.
     *
     * fixedDelay: espera 5 min despues de que termina la ejecucion anterior
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    @Transactional
    public void finalizarReservasVencidas() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        List<Reserva> vencidas = reservaRepository.reservasParaFinalizar(hoy, ahora);
        if (vencidas.isEmpty()) return;

        for (Reserva r : vencidas) {
            r.setEstado(EstadoReserva.FINALIZADA);
        }
        reservaRepository.saveAll(vencidas);
        log.info("[Job] Marcadas {} reservas como FINALIZADAS", vencidas.size());
    }

    /** Genera un codigo de voucher unico tipo "XPLR-A1B2C3D4" */
    private String generarVoucher() {
        return "XPLR-" + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
