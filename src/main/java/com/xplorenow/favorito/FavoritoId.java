package com.xplorenow.favorito;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clave primaria compuesta de la tabla favoritos.
 * Un usuario solo puede marcar una actividad como favorita una vez.
 */
@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FavoritoId implements Serializable {

    private Long usuarioId;
    private Long actividadId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoritoId that)) return false;
        return Objects.equals(usuarioId, that.usuarioId)
                && Objects.equals(actividadId, that.actividadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, actividadId);
    }
}
