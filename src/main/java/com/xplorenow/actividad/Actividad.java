package com.xplorenow.actividad;

import com.xplorenow.categoria.Categoria;
import com.xplorenow.destino.Destino;
import com.xplorenow.foto.Foto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actividades")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String imagenPrincipal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /** Duracion en minutos */
    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cuposDisponibles;

    @Column(nullable = false)
    private LocalDate fechaDisponibleDesde;

    @Column(nullable = false)
    private LocalDate fechaDisponibleHasta;

    // ===== Datos del detalle =====

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String queIncluye;

    @Column(length = 300)
    private String puntoEncuentro;

    /** Latitud del punto de encuentro (para el mapa del punto 10) */
    private Double latitud;

    /** Longitud del punto de encuentro */
    private Double longitud;

    @Column(length = 100)
    private String guiaAsignado;

    @Column(length = 50)
    private String idioma;

    @Column(columnDefinition = "TEXT")
    private String politicaCancelacion;

    @Builder.Default
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
}
