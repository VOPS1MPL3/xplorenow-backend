package com.xplorenow.actividad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    /**
     * Busqueda con filtros combinados. Cualquier parametro puede ser null
     * y la condicion se ignora (gracias a los OR ... IS NULL).
     */
    @Query("""
        SELECT a FROM Actividad a
        WHERE (:destinoId IS NULL OR a.destino.id = :destinoId)
          AND (:categoriaId IS NULL OR a.categoria.id = :categoriaId)
          AND (:fecha IS NULL OR (a.fechaDisponibleDesde <= :fecha
                                  AND a.fechaDisponibleHasta >= :fecha))
          AND (:precioMin IS NULL OR a.precio >= :precioMin)
          AND (:precioMax IS NULL OR a.precio <= :precioMax)
        """)
    Page<Actividad> buscarConFiltros(
            @Param("destinoId") Long destinoId,
            @Param("categoriaId") Long categoriaId,
            @Param("fecha") LocalDate fecha,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );

    /**
     * Destacadas / recomendadas: actividades cuya categoria coincide con
     * las preferencias del usuario. Si la lista viene vacia/null, devuelve top 10.
     */
    @Query("""
        SELECT a FROM Actividad a
        WHERE (:codigosCategorias IS NULL
               OR a.categoria.codigo IN :codigosCategorias)
        ORDER BY a.cuposDisponibles DESC
        """)
    List<Actividad> buscarDestacadas(
            @Param("codigosCategorias") List<String> codigosCategorias,
            Pageable pageable
    );
}
