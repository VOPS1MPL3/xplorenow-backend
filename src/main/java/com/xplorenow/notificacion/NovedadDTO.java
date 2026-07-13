package com.xplorenow.notificacion;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NovedadDTO {

    private Long id;
    private Long reservaId;
    private TipoNovedad tipo;
    private String mensaje;
    private LocalDateTime fecha;

    public static NovedadDTO desde(Novedad n) {
        return NovedadDTO.builder()
                .id(n.getId())
                .reservaId(n.getReserva().getId())
                .tipo(n.getTipo())
                .mensaje(n.getMensaje())
                .fecha(n.getFecha())
                .build();
    }
}
