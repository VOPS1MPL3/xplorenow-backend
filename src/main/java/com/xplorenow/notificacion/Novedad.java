package com.xplorenow.notificacion;

import com.xplorenow.reserva.Reserva;
import com.xplorenow.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Novedad destinada a un usuario (Punto 12 del TPO).
 * Cada fila es un evento que el front todavía no vio: se consultan
 * via long polling en /notificaciones/novedades (Punto 12, ver PR2).
 */
@Entity
@Table(name = "novedades")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Novedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoNovedad tipo;

    @Column(nullable = false, length = 300)
    private String mensaje;

    /** Timestamp de creación. Se usa como cursor para el long polling (?ultimaFecha=). */
    @Column(nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
