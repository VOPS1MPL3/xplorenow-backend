package com.xplorenow.reserva;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO completo de la reserva con todos los datos para mostrar el voucher.
 * Pensado para el modo offline del punto 8 del TPO: la app guarda este
 * objeto en Room y lo muestra sin conexion.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservaDetalleDTO {

    private Long id;
    private String voucherCodigo;
    private EstadoReserva estado;

    // Actividad
    private Long actividadId;
    private String actividadNombre;
    private String actividadImagen;
    private String destino;
    private String categoria;
    private Integer duracionMinutos;
    private String guiaAsignado;
    private String idioma;

    // Horario
    private LocalDate fecha;
    private LocalTime hora;

    // Punto de encuentro (importante para el voucher)
    private String puntoEncuentro;
    private Double latitud;
    private Double longitud;

    private Integer cantidadParticipantes;
    private String politicaCancelacion;

    private LocalDateTime creadaEn;
    private LocalDateTime canceladaEn;

    public static ReservaDetalleDTO desde(Reserva r) {
        var act = r.getActividad();
        return ReservaDetalleDTO.builder()
                .id(r.getId())
                .voucherCodigo(r.getVoucherCodigo())
                .estado(r.getEstado())
                .actividadId(act.getId())
                .actividadNombre(act.getNombre())
                .actividadImagen(act.getImagenPrincipal())
                .destino(act.getDestino().getNombre())
                .categoria(act.getCategoria().getNombre())
                .duracionMinutos(act.getDuracionMinutos())
                .guiaAsignado(act.getGuiaAsignado())
                .idioma(act.getIdioma())
                .fecha(r.getHorario().getFecha())
                .hora(r.getHorario().getHora())
                .puntoEncuentro(act.getPuntoEncuentro())
                .latitud(act.getLatitud())
                .longitud(act.getLongitud())
                .cantidadParticipantes(r.getCantidadParticipantes())
                .politicaCancelacion(act.getPoliticaCancelacion())
                .creadaEn(r.getCreadaEn())
                .canceladaEn(r.getCanceladaEn())
                .build();
    }
}
