package com.xplorenow.auth;

import com.xplorenow.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Genera, envia y valida codigos OTP de 6 digitos.
 * Cada codigo dura 5 minutos y se invalida al usarse.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private static final int VIGENCIA_MINUTOS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    /**
     * Genera un nuevo OTP de 6 digitos, lo guarda en BD
     * y lo envia por email al usuario.
     */
    public String generarYEnviar(Usuario usuario) {
        String codigo = String.format("%06d", RANDOM.nextInt(1_000_000));

        Otp otp = Otp.builder()
                .usuario(usuario)
                .codigo(codigo)
                .expiraEn(LocalDateTime.now().plusMinutes(VIGENCIA_MINUTOS))
                .usado(false)
                .build();
        otpRepository.save(otp);

        emailService.enviarOtp(usuario.getEmail(), codigo);

        return codigo;
    }

    /**
     * Valida que el codigo coincida con el ultimo OTP no usado del usuario,
     * y que no haya expirado. Si todo OK, lo marca como usado.
     *
     * @return true si el OTP es valido
     */
    public boolean validar(Usuario usuario, String codigo) {
        Optional<Otp> opt = otpRepository
                .findFirstByUsuarioAndUsadoFalseOrderByCreadoEnDesc(usuario);

        if (opt.isEmpty()) return false;

        Otp otp = opt.get();
        if (!otp.getCodigo().equals(codigo)) return false;
        if (otp.getExpiraEn().isBefore(LocalDateTime.now())) return false;

        otp.setUsado(true);
        otpRepository.save(otp);
        return true;
    }
}
