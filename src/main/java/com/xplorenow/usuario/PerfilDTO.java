package com.xplorenow.usuario;

import com.xplorenow.categoria.CategoriaDTO;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PerfilDTO {

    private Long id;
    private String email;
    private String nombre;
    private String telefono;
    private String fotoUrl;
    private List<CategoriaDTO> preferencias;

    public static PerfilDTO desde(Usuario u) {
        return PerfilDTO.builder()
                .id(u.getId())
                .email(u.getEmail())
                .nombre(u.getNombre())
                .telefono(u.getTelefono())
                .fotoUrl(u.getFotoUrl())
                .preferencias(u.getPreferencias().stream()
                        .map(CategoriaDTO::desde)
                        .toList())
                .build();
    }
}
