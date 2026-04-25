package com.xplorenow.destino;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DestinoDTO {
    private Long id;
    private String nombre;
    private String pais;

    public static DestinoDTO desde(Destino d) {
        return DestinoDTO.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .pais(d.getPais())
                .build();
    }
}
