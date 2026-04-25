package com.xplorenow.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * STUB de autenticacion para que la app Android pueda probar el flujo JWT.
 * En la version final, esto se reemplaza por:
 *   - Validacion contra tabla de usuarios en BD
 *   - Flujo OTP del punto 1 del TPO
 *
 * Por ahora acepta CUALQUIER email/password y devuelve un token valido.
 * Esto es a proposito: sirve para que el grupo pueda integrar la app
 * sin esperar a que se termine el modulo de Usuarios.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        // TODO: cuando este listo el modulo Usuarios, validar contra BD
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String token = jwtService.generarToken(req.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
