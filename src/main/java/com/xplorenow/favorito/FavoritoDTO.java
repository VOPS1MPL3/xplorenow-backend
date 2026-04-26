package com.xplorenow.favorito;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO de favorito para el listado "Mis favoritos".
 * Incluye los datos de la actividad + un flag tieneNovedad
 * que la app usa para mostrar el indicador visual del punto 16.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FavoritoDTO {

    private Long actividadId;
    private String actividadNombre;
    private String actividadImagen;
    private String destino;
    private String categoria;
    private Integer duracionMinutos;

    private BigDecimal precioActual;
    private Integer cuposActuales;

    private BigDecimal precioAlMarcar;
    private Integer cuposAlMarcar;

    /** true si bajo de precio o liberaron cupos desde que se marco */
    private boolean tieneNovedad;
    private String motivoNovedad; // "BAJO_PRECIO" / "MAS_CUPOS" / null

    public static FavoritoDTO desde(Favorito f) {
        var act = f.getActividad();
        BigDecimal precioActual = act.getPrecio();
        Integer cuposActuales = act.getCuposDisponibles();

        boolean bajoPrecio = precioActual.compareTo(f.getPrecioAlMarcar()) < 0;
        boolean masCupos = cuposActuales > f.getCuposAlMarcar();

        String motivo = null;
        if (bajoPrecio) motivo = "BAJO_PRECIO";
        else if (masCupos) motivo = "MAS_CUPOS";

        return FavoritoDTO.builder()
                .actividadId(act.getId())
                .actividadNombre(act.getNombre())
                .actividadImagen(act.getImagenPrincipal())
                .destino(act.getDestino().getNombre())
                .categoria(act.getCategoria().getNombre())
                .duracionMinutos(act.getDuracionMinutos())
                .precioActual(precioActual)
                .cuposActuales(cuposActuales)
                .precioAlMarcar(f.getPrecioAlMarcar())
                .cuposAlMarcar(f.getCuposAlMarcar())
                .tieneNovedad(bajoPrecio || masCupos)
                .motivoNovedad(motivo)
                .build();
    }
}
