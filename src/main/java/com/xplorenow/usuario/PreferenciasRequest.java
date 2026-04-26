package com.xplorenow.usuario;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PreferenciasRequest {
    /** Lista de ids de categorias que le interesan al usuario */
    private List<Long> categoriaIds;
}
