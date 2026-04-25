package com.xplorenow.destino;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "destinos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String pais;
}
