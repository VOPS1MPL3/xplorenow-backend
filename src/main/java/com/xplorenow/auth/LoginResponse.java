package com.xplorenow.auth;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LoginResponse {
    /** Coincide con el contrato del apunte: { "token": "eyJ..." } */
    private String token;
}
