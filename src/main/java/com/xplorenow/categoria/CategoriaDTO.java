package com.xplorenow.categoria;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long id;
    private String codigo;
    private String nombre;

    public static CategoriaDTO desde(Categoria c) {
        return CategoriaDTO.builder()
                .id(c.getId())
                .codigo(c.getCodigo())
                .nombre(c.getNombre())
                .build();
    }
}
