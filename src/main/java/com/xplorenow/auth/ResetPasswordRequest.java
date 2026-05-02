package com.xplorenow.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Body de POST /auth/password/reset.
 * Recibe el email, el OTP recibido por correo y la nueva contraseña.
 */
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    private String email;
    private String codigo;
    private String nuevaPassword;
}
