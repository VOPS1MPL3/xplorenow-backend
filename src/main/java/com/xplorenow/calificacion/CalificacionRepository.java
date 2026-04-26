package com.xplorenow.calificacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    Optional<Calificacion> findByReservaId(Long reservaId);

    boolean existsByReservaId(Long reservaId);
}
