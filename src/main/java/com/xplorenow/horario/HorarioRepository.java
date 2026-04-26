package com.xplorenow.horario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    /** Horarios de una actividad en una fecha, ordenados por hora ascendente */
    List<Horario> findByActividadIdAndFechaOrderByHoraAsc(Long actividadId, LocalDate fecha);
}
