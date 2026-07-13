package com.xplorenow.horario;

import com.xplorenow.notificacion.NovedadService;
import com.xplorenow.reserva.EstadoReserva;
import com.xplorenow.reserva.Reserva;
import com.xplorenow.reserva.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final ReservaRepository reservaRepository;
    private final NovedadService novedadService;

    /**
     * Reprograma un horario (simula una accion de la operadora, ver
     * OperadorController) y genera una Novedad de REPROGRAMACION para
     * cada reserva CONFIRMADA que dependia de ese horario.
     */
    @Transactional
    public Horario reprogramar(Long horarioId, LocalDate nuevaFecha, LocalTime nuevaHora) {
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

        LocalDate fechaAnterior = horario.getFecha();
        LocalTime horaAnterior = horario.getHora();

        horario.setFecha(nuevaFecha);
        horario.setHora(nuevaHora);
        Horario guardado = horarioRepository.save(horario);

        List<Reserva> afectadas = reservaRepository
                .findByHorarioIdAndEstado(horarioId, EstadoReserva.CONFIRMADA);

        for (Reserva r : afectadas) {
            novedadService.registrarReprogramacion(r, fechaAnterior, horaAnterior);
        }

        return guardado;
    }
}
