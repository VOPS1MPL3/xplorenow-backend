package com.xplorenow.reserva;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CrearReservaRequest {
    private Long horarioId;
    private Integer cantidadParticipantes;
}
