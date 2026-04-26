package com.xplorenow.reserva;

import com.xplorenow.actividad.Actividad;
import com.xplorenow.horario.Horario;
import com.xplorenow.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @Column(nullable = false)
    private Integer cantidadParticipantes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado;

    @Column(nullable = false, unique = true, length = 30)
    private String voucherCodigo;

    @Column(nullable = false)
    private LocalDateTime creadaEn;

    private LocalDateTime canceladaEn;

    /**
     * Relacion inversa hacia Calificacion. Cada reserva FINALIZADA puede
     * tener cero o una calificacion. Se carga lazy para no traerla siempre.
     */
    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private com.xplorenow.calificacion.Calificacion calificacion;

    @PrePersist
    void prePersist() {
        if (creadaEn == null) creadaEn = LocalDateTime.now();
        if (estado == null) estado = EstadoReserva.CONFIRMADA;
    }
}
