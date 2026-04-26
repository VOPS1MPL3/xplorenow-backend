package com.xplorenow.auth;

import com.xplorenow.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    /** Busca el ultimo OTP no usado del usuario para validar el codigo */
    Optional<Otp> findFirstByUsuarioAndUsadoFalseOrderByCreadoEnDesc(Usuario usuario);
}
