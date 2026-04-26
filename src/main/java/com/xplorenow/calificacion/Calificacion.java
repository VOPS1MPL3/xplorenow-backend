package com.xplorenow.calificacion;

import com.xplorenow.reserva.Reserva;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Calificacion 1-a-1 con una Reserva FINALIZADA.
 * Cumple con los puntos 10-13 del TPO:
 *   - estrellas (1-5) de actividad y guia por separado
 *   - comentario opcional max 300 chars
 *   - una sola por reserva (UNIQUE en reserva_id)
 */
@Entity
@Table(name = "calificaciones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false, unique = true)
    private Reserva reserva;

    @Column(nullable = false)
    private Integer ratingActividad;

    @Column(nullable = false)
    private Integer ratingGuia;

    @Column(length = 300)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime creadaEn;

    @PrePersist
    void prePersist() {
        if (creadaEn == null) creadaEn = LocalDateTime.now();
    }
}
