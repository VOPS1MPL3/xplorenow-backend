package com.xplorenow.actividad;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO liviano para el listado del catalogo (Home).
 * No incluye descripcion, fotos ni datos del detalle.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ActividadDTO {

    private Long id;
    private String nombre;
    private String imagenPrincipal;
    private String destino;
    private String categoria;
    private Integer duracionMinutos;
    private BigDecimal precio;
    private Integer cuposDisponibles;

    public static ActividadDTO desde(Actividad a) {
        return ActividadDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .imagenPrincipal(a.getImagenPrincipal())
                .destino(a.getDestino().getNombre())
                .categoria(a.getCategoria().getNombre())
                .duracionMinutos(a.getDuracionMinutos())
                .precio(a.getPrecio())
                .cuposDisponibles(a.getCuposDisponibles())
                .build();
    }
}
