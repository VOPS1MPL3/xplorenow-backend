package com.xplorenow.calificacion;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CalificacionDTO {

    private Long id;
    private Integer ratingActividad;
    private Integer ratingGuia;
    private String comentario;
    private LocalDateTime creadaEn;

    public static CalificacionDTO desde(Calificacion c) {
        return CalificacionDTO.builder()
                .id(c.getId())
                .ratingActividad(c.getRatingActividad())
                .ratingGuia(c.getRatingGuia())
                .comentario(c.getComentario())
                .creadaEn(c.getCreadaEn())
                .build();
    }
}
