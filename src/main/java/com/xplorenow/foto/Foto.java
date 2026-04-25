package com.xplorenow.foto;

import com.xplorenow.actividad.Actividad;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fotos_actividad")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;
}
