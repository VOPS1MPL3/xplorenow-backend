package com.xplorenow.auth;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegistroRequest {
    private String email;
    private String password;
    private String nombre;
    private String telefono;
}
