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
 *   POST /auth/registro          -> Crear usuario nuevo
 *   POST /auth/login             -> Login clasico email + password (devuelve JWT)
 *   POST /auth/otp/solicitar     -> Pedir codigo OTP (loguea en consola)
 *   POST /auth/otp/confirmar     -> Confirmar codigo OTP (devuelve JWT)
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
        System.out.println("==> LOGIN REQUEST: email=[" + req.getEmail() + "] password=[" + req.getPassword() + "]");
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        System.out.println("==> Usuario encontrado: " + opt.isPresent());
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(error("Credenciales invalidas"));
        }

        Usuario u = opt.get();
        boolean matches = passwordEncoder.matches(req.getPassword(), u.getPasswordHash());
        System.out.println("==> Hash en BD: [" + u.getPasswordHash() + "]");
        System.out.println("==> Password matches: " + matches);
        if (!matches) {
            return ResponseEntity.status(401).body(error("Credenciales invalidas"));
        }

        String token = jwtService.generarToken(u.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/otp/solicitar")
    public ResponseEntity<?> solicitarOtp(@RequestBody SolicitarOtpRequest req) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(req.getEmail());
        if (opt.isEmpty()) {
            // Por seguridad respondemos OK aunque no exista,
            // asi no revelamos que emails estan registrados
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

    private static java.util.Map<String, String> error(String msg) {
        return java.util.Map.of("error", msg);
    }
}
