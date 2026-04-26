package com.xplorenow.auth;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ConfirmarOtpRequest {
    private String email;
    private String codigo;
}
