package com.xplorenow.actividad;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO completo para la pantalla de detalle de actividad.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActividadDetalleDTO {

    private Long id;
    private String nombre;
    private String imagenPrincipal;
    private String destino;
    private String categoria;
    private Integer duracionMinutos;
    private BigDecimal precio;
    private Integer cuposDisponibles;

    private String descripcion;
    private String queIncluye;
    private String puntoEncuentro;
    private Double latitud;
    private Double longitud;
    private String guiaAsignado;
    private String idioma;
    private String politicaCancelacion;
    private List<String> galeriaUrls;

    public static ActividadDetalleDTO desde(Actividad a) {
        return ActividadDetalleDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .imagenPrincipal(a.getImagenPrincipal())
                .destino(a.getDestino().getNombre())
                .categoria(a.getCategoria().getNombre())
                .duracionMinutos(a.getDuracionMinutos())
                .precio(a.getPrecio())
                .cuposDisponibles(a.getCuposDisponibles())
                .descripcion(a.getDescripcion())
                .queIncluye(a.getQueIncluye())
                .puntoEncuentro(a.getPuntoEncuentro())
                .latitud(a.getLatitud())
                .longitud(a.getLongitud())
                .guiaAsignado(a.getGuiaAsignado())
                .idioma(a.getIdioma())
                .politicaCancelacion(a.getPoliticaCancelacion())
                .galeriaUrls(a.getFotos().stream()
                        .map(f -> f.getUrl())
                        .toList())
                .build();
    }
}
