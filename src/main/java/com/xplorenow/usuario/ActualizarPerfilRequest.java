package com.xplorenow.usuario;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ActualizarPerfilRequest {
    private String nombre;
    private String telefono;
    private String fotoUrl;
}
