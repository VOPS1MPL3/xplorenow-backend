package com.xplorenow.notificacion;

import com.xplorenow.reserva.Reserva;
import com.xplorenow.reserva.ReservaRepository;
import com.xplorenow.usuario.Usuario;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Genera las Novedades del Punto 12 del TPO:
 *  - Recordatorio 24hs antes de la actividad (job programado).
 *  - Cancelacion / Reprogramacion disparadas por el "operador" (ver
 *    OperadorController, que simula el backoffice de la operadora ya que
 *    el TPO no pide un rol admin real).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NovedadService {

    private final NovedadRepository novedadRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public void registrarCancelacion(Reserva reserva) {
        Novedad n = Novedad.builder()
                .usuario(reserva.getUsuario())
                .reserva(reserva)
                .tipo(TipoNovedad.CANCELACION)
                .mensaje("Tu actividad \"" + reserva.getActividad().getNombre()
                        + "\" fue cancelada por la operadora.")
                .build();
        novedadRepository.save(n);
        log.info("[Novedad] CANCELACION generada para reserva {}", reserva.getId());
    }
    
    /**
     * Marca como leidas las novedades ya entregadas por el long polling,
     * para que no se repitan al reabrir la app.
     */
    @Transactional
    public void marcarLeidas(List<Novedad> novedades) {
        for (Novedad n : novedades) {
            n.setLeida(true);
        }
        novedadRepository.saveAll(novedades);
    }

    @Transactional
    public void registrarReprogramacion(Reserva reserva, LocalDate fechaAnterior, LocalTime horaAnterior,
                                        LocalDate fechaNueva, LocalTime horaNueva) {
        Novedad n = Novedad.builder()
                .usuario(reserva.getUsuario())
                .reserva(reserva)
                .tipo(TipoNovedad.REPROGRAMACION)
                .mensaje("Tu actividad \"" + reserva.getActividad().getNombre()
                        + "\" fue reprogramada. Antes: " + fechaAnterior + " " + horaAnterior
                        + " -> Ahora: " + fechaNueva + " " + horaNueva)
                .build();
        novedadRepository.save(n);
        log.info("[Novedad] REPROGRAMACION generada para reserva {}", reserva.getId());
    }

    /**
     * Devuelve las novedades que el usuario todavia no recibio y las marca
     * como leidas. Se llama una sola vez al arrancar la sesion: el long
     * polling solo ve novedades posteriores al momento de conexion, asi que
     * sin esto se perderian las generadas con la app cerrada (tipicamente el
     * recordatorio de 24hs, que lo dispara un job programado).
     */
    @Transactional
    public List<Novedad> obtenerPendientesYMarcarLeidas(Usuario usuario) {
        List<Novedad> pendientes = novedadRepository.findByUsuarioAndLeidaFalseOrderByFechaAsc(usuario);
        for (Novedad n : pendientes) {
            n.setLeida(true);
        }
        novedadRepository.saveAll(pendientes);
        return pendientes;
    }

    /**
     * Cada 5 minutos busca reservas CONFIRMADAS cuyo horario cae dentro de
     * las proximas 24hs y todavia no recibieron el recordatorio, y genera
     * la Novedad correspondiente.
     *
     * El filtro grueso (por fecha) se hace en la query JPQL; el filtro fino
     * (fecha+hora exacta dentro de la ventana de 24hs) se hace en Java,
     * mismo criterio que usa ReservaService.finalizarReservasVencidas().
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    @Transactional
    public void generarRecordatorios24h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusHours(24);

        List<Reserva> candidatas = reservaRepository
                .candidatasParaRecordatorio(ahora.toLocalDate(), limite.toLocalDate());

        for (Reserva r : candidatas) {
            LocalDateTime fechaHoraActividad = LocalDateTime.of(r.getHorario().getFecha(), r.getHorario().getHora());
            if (!fechaHoraActividad.isBefore(ahora) && !fechaHoraActividad.isAfter(limite)) {
                Novedad n = Novedad.builder()
                        .usuario(r.getUsuario())
                        .reserva(r)
                        .tipo(TipoNovedad.RECORDATORIO_24H)
                        .mensaje("Recordatorio: tu actividad \"" + r.getActividad().getNombre()
                                + "\" es en menos de 24hs, el " + r.getHorario().getFecha()
                                + " a las " + r.getHorario().getHora())
                        .build();
                novedadRepository.save(n);

                r.setRecordatorio24hEnviado(true);
                reservaRepository.save(r);
                log.info("[Novedad] RECORDATORIO_24H generado para reserva {}", r.getId());
            }
        }
    }
}
