package com.xplorenow.auth;

import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Endpoints de autenticacion (Punto 1 del TPO).
 *
 *   POST /auth/registro              -> Crear usuario nuevo
 *   POST /auth/login                 -> Login clasico email + password (devuelve JWT)
 *   POST /auth/otp/solicitar         -> Pedir codigo OTP
 *   POST /auth/otp/confirmar         -> Confirmar codigo OTP (devuelve JWT)
 *   POST /auth/password/olvide       -> Solicitar codigo para resetear password
 *   POST /auth/password/reset        -> Validar codigo y cambiar password
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest req) {
        if (req.getEmail() == null || req.getPassword() == null
                || req.getNombre() == null) {
            return ResponseEntity.badRequest()
                    .body(error("email, password y nombre son obligatorios"));
        }
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(error("Ese email ya esta registrado"));
        }

        Usuario u = Usuario.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .nombre(req.getNombre())
                .telefono(req.getTelefono())
                .build();
        usuarioRepository.save(u);

        String token = jwtService.generarToken(u.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(error("Credenciales invalidas"));
        }

        Usuario u = opt.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            return ResponseEntity.status(401).body(error("Credenciales invalidas"));
        }

        String token = jwtService.generarToken(u.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/otp/solicitar")
    public ResponseEntity<?> solicitarOtp(@RequestBody SolicitarOtpRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            // Por seguridad respondemos OK aunque no exista
            return ResponseEntity.ok().build();
        }
        otpService.generarYEnviar(opt.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/otp/confirmar")
    public ResponseEntity<?> confirmarOtp(@RequestBody ConfirmarOtpRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(error("Codigo invalido"));
        }

        if (!otpService.validar(opt.get(), req.getCodigo())) {
            return ResponseEntity.status(401).body(error("Codigo invalido o expirado"));
        }

        String token = jwtService.generarToken(opt.get().getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // =========================================================
    // Recupero de contraseña
    // =========================================================

    /**
     * Paso 1: el usuario ingresa su email.
     * Si existe, le mandamos un OTP por email.
     * Siempre respondemos 200 para no revelar qué emails están registrados.
     */
    @PostMapping("/password/olvide")
    public ResponseEntity<?> olvideClave(@RequestBody SolicitarOtpRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        opt.ifPresent(u -> otpService.generarYEnviar(u));
        return ResponseEntity.ok().build();
    }

    /**
     * Paso 2: el usuario ingresa el OTP + nueva contraseña.
     * Si el OTP es válido, actualizamos el hash y respondemos 200.
     * Si el OTP es inválido o expiró, respondemos 401.
     */
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetClave(@RequestBody ResetPasswordRequest req) {
        if (req.getEmail() == null || req.getCodigo() == null
                || req.getNuevaPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(error("email, codigo y nuevaPassword son obligatorios"));
        }

        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(error("Codigo invalido o expirado"));
        }

        Usuario u = opt.get();
        if (!otpService.validar(u, req.getCodigo())) {
            return ResponseEntity.status(401).body(error("Codigo invalido o expirado"));
        }

        // OTP válido → actualizamos la contraseña
        u.setPasswordHash(passwordEncoder.encode(req.getNuevaPassword()));
        usuarioRepository.save(u);

        return ResponseEntity.ok().build();
    }

    private static java.util.Map<String, String> error(String msg) {
        return java.util.Map.of("error", msg);
    }
}
