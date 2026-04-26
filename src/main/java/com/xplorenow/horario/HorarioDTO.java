package com.xplorenow.horario;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HorarioDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer cuposRestantes;

    public static HorarioDTO desde(Horario h) {
        return HorarioDTO.builder()
                .id(h.getId())
                .fecha(h.getFecha())
                .hora(h.getHora())
                .cuposRestantes(h.getCuposRestantes())
                .build();
    }
}
