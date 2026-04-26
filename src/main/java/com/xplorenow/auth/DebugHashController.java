package com.xplorenow.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SOLO PARA DEBUG - borrar despues de confirmar el flujo de auth.
 */
@RestController
@RequiredArgsConstructor
public class DebugHashController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/debug/hash")
    public Map<String, String> hash(@RequestParam String texto) {
        return Map.of(
            "input", texto,
            "hash", passwordEncoder.encode(texto)
        );
    }
}