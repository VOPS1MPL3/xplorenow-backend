package com.xplorenow.noticia;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NoticiaDTO {

    private Long id;
    private String titulo;
    private String descripcionBreve;
    private String descripcionCompleta;
    private String imagenUrl;
    private LocalDateTime publicadaEn;

    /** Si la noticia apunta a una actividad, este id le dice a la app
     *  que tiene que navegar al detalle de esa actividad al tocarla. */
    private Long actividadRelacionadaId;

    public static NoticiaDTO desde(Noticia n) {
        return NoticiaDTO.builder()
                .id(n.getId())
                .titulo(n.getTitulo())
                .descripcionBreve(n.getDescripcionBreve())
                .descripcionCompleta(n.getDescripcionCompleta())
                .imagenUrl(n.getImagenUrl())
                .publicadaEn(n.getPublicadaEn())
                .actividadRelacionadaId(
                        n.getActividadRelacionada() != null
                                ? n.getActividadRelacionada().getId()
                                : null)
                .build();
    }
}
