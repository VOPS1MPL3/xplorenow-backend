package com.xplorenow.calificacion;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CrearCalificacionRequest {
    private Integer ratingActividad;
    private Integer ratingGuia;
    private String comentario;
}
