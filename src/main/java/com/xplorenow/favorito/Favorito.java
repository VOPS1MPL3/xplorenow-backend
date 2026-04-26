package com.xplorenow.favorito;

import com.xplorenow.actividad.Actividad;
import com.xplorenow.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tabla puente usuario <-> actividad para favoritos.
 * Guarda un "snapshot" del precio y los cupos al momento de marcarla,
 * para detectar novedades (punto 16 del TPO):
 *   - Si el precio actual < precio_al_marcar -> bajo de precio
 *   - Si los cupos actuales > cupos_al_marcar -> liberaron cupos
 */
@Entity
@Table(name = "favoritos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Favorito {

    @EmbeddedId
    private FavoritoId id;

    @MapsId("usuarioId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @MapsId("actividadId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    @Column(nullable = false)
    private LocalDateTime marcadaEn;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioAlMarcar;

    @Column(nullable = false)
    private Integer cuposAlMarcar;

    @PrePersist
    void prePersist() {
        if (marcadaEn == null) marcadaEn = LocalDateTime.now();
    }
}
