package com.xplorenow.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio para envio de emails transaccionales.
 * Actualmente usado para enviar OTPs de verificacion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${xplorenow.mail.from}")
    private String from;

    /**
     * Envia el codigo OTP al email del usuario.
     * Si el envio falla, loguea el error y el codigo por consola como fallback.
     */
    public void enviarOtp(String destinatario, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(destinatario);
            helper.setSubject("Tu código de acceso XploreNow");
            helper.setText(buildHtml(codigo), true);

            mailSender.send(message);
            log.info("[EMAIL] OTP enviado correctamente a {}", destinatario);

        } catch (MessagingException e) {
            // Fallback: si el mail falla, seguimos logueando el codigo para no romper el flujo
            log.error("[EMAIL ERROR] No se pudo enviar OTP a {}: {}", destinatario, e.getMessage());
            log.info("==========================================");
            log.info("[FALLBACK] OTP para {}: {}", destinatario, codigo);
            log.info("==========================================");
        }
    }

    private String buildHtml(String codigo) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto;">
                    <h2 style="color: #2563EB;">XploreNow</h2>
                    <p style="color: #334155;">Tu código de verificación es:</p>
                    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px;
                                background: #F1F5F9; padding: 16px; text-align: center;
                                border-radius: 8px; color: #1E293B; margin: 16px 0;">
                        %s
                    </div>
                    <p style="color: #64748B; font-size: 13px;">
                        Válido por <strong>5 minutos</strong>.
                        Si no solicitaste este código, ignorá este mail.
                    </p>
                    <hr style="border: none; border-top: 1px solid #E2E8F0; margin-top: 24px;">
                    <p style="color: #94A3B8; font-size: 11px; text-align: center;">
                        XploreNow · Plataforma de experiencias turísticas
                    </p>
                </div>
                """.formatted(codigo);
    }
}
