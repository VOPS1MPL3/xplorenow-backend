package com.xplorenow.categoria;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Codigo interno: AVENTURA, CULTURA, GASTRONOMIA, NATURALEZA, RELAX,
     * FREE_TOUR, VISITA_GUIADA, EXCURSION
     */
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;
}
