package com.xplorenow.auth;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SolicitarOtpRequest {
    private String email;
}
