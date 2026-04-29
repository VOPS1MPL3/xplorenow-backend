package com.xplorenow.reserva;

import com.xplorenow.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /** Mis reservas, todas o filtradas por estado, ordenadas por fecha de horario descendente */
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.usuario = :usuario
          AND (:estado IS NULL OR r.estado = :estado)
        ORDER BY r.horario.fecha DESC, r.horario.hora DESC
        """)
    List<Reserva> misReservas(
            @Param("usuario") Usuario usuario,
            @Param("estado") EstadoReserva estado);

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.usuario = :usuario
          AND r.estado = com.xplorenow.reserva.EstadoReserva.FINALIZADA
          AND (:destinoId IS NULL OR r.actividad.destino.id = :destinoId)
          AND (CAST(:fechaDesde AS LocalDate) IS NULL OR r.horario.fecha >= :fechaDesde)
          AND (CAST(:fechaHasta AS LocalDate) IS NULL OR r.horario.fecha <= :fechaHasta)
        ORDER BY r.horario.fecha DESC
        """)  
    List<Reserva> historial(
            @Param("usuario") Usuario usuario,
            @Param("destinoId") Long destinoId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);

    /**
     * Reservas que hay que marcar como FINALIZADAS:
     * estado CONFIRMADA y la fecha+hora del horario ya paso.
     * Usada por el job programado.
     */
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.estado = com.xplorenow.reserva.EstadoReserva.CONFIRMADA
          AND (r.horario.fecha < :hoy
               OR (r.horario.fecha = :hoy AND r.horario.hora <= :ahora))
        """)
    List<Reserva> reservasParaFinalizar(
            @Param("hoy") LocalDate hoy,
            @Param("ahora") java.time.LocalTime ahora);
}
