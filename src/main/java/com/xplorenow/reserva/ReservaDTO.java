package com.xplorenow.reserva;

import com.xplorenow.calificacion.CalificacionDTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservaDTO {

    private Long id;
    private String voucherCodigo;
    private EstadoReserva estado;

    // Datos de la actividad (para no obligar a la app a hacer otra request)
    private Long actividadId;
    private String actividadNombre;
    private String actividadImagen;
    private String destino;

    // Datos del horario
    private LocalDate fecha;
    private LocalTime hora;

    private Integer cantidadParticipantes;
    private Integer duracionMinutos;
    private String guiaAsignado;

    /**
     * Calificacion adjunta si la reserva esta FINALIZADA y el usuario
     * ya califico. null si no califico todavia o no aplica.
     * Cumple punto 13 del TPO: "la calificacion queda visible en el historial".
     */
    private CalificacionDTO calificacion;

    public static ReservaDTO desde(Reserva r) {
        CalificacionDTO calif = r.getCalificacion() != null
                ? CalificacionDTO.desde(r.getCalificacion())
                : null;

        return ReservaDTO.builder()
                .id(r.getId())
                .voucherCodigo(r.getVoucherCodigo())
                .estado(r.getEstado())
                .actividadId(r.getActividad().getId())
                .actividadNombre(r.getActividad().getNombre())
                .actividadImagen(r.getActividad().getImagenPrincipal())
                .destino(r.getActividad().getDestino().getNombre())
                .fecha(r.getHorario().getFecha())
                .hora(r.getHorario().getHora())
                .cantidadParticipantes(r.getCantidadParticipantes())
                .duracionMinutos(r.getActividad().getDuracionMinutos())
                .guiaAsignado(r.getActividad().getGuiaAsignado())
                .calificacion(calif)
                .build();
    }
}
