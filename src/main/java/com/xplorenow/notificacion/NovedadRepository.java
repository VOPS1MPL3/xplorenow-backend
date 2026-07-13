package com.xplorenow.notificacion;

import com.xplorenow.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NovedadRepository extends JpaRepository<Novedad, Long> {

    /**
     * Novedades de un usuario posteriores a una fecha, ordenadas ascendente.
     * Usado por el endpoint de long polling (PR2) para saber que hay "desde"
     * el ultimo chequeo del cliente.
     */
    List<Novedad> findByUsuarioAndFechaAfterOrderByFechaAsc(Usuario usuario, LocalDateTime fecha);
}
