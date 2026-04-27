package com.xplorenow.noticia;

import com.xplorenow.actividad.Actividad;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Noticia / oferta / destino destacado (Punto 9 del TPO).
 * Puede o no estar asociada a una actividad: si lo esta, la app
 * redirige al detalle de esa actividad; si no, muestra el detalle
 * de la noticia.
 */
@Entity
@Table(name = "noticias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String descripcionBreve;

    @Column(columnDefinition = "TEXT")
    private String descripcionCompleta;

    @Column(nullable = false, length = 500)
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_relacionada_id")
    private Actividad actividadRelacionada;

    @Column(nullable = false)
    private LocalDateTime publicadaEn;

    @PrePersist
    void prePersist() {
        if (publicadaEn == null) publicadaEn = LocalDateTime.now();
    }
}
