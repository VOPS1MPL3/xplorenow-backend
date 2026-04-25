package com.xplorenow.actividad;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Endpoints del Catalogo de Actividades (Punto 3 del TPO).
 *
 *   GET /actividades                 -> Listado paginado con filtros
 *   GET /actividades/{id}            -> Detalle completo
 *   GET /actividades/destacadas      -> Recomendadas segun preferencias
 */
@RestController
@RequestMapping("/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadRepository repository;

    /**
     * Listado paginado con filtros combinados. Todos opcionales.
     *
     * Ejemplo: GET /actividades?destinoId=1&categoriaId=3&precioMax=15000&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<ActividadDTO>> listar(
            @RequestParam(required = false) Long destinoId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Actividad> resultado = repository.buscarConFiltros(
                destinoId, categoriaId, fecha, precioMin, precioMax, pageable);

        Page<ActividadDTO> dtos = resultado.map(ActividadDTO::desde);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Detalle completo de una actividad (descripcion, fotos, guia, etc.).
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActividadDetalleDTO> detalle(@PathVariable Long id) {
        return repository.findById(id)
                .map(ActividadDetalleDTO::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Destacadas / recomendadas. Recibe preferencias del perfil.
     * Si "categorias" llega vacio, devuelve todas ordenadas por cupos.
     *
     * Ejemplo: GET /actividades/destacadas?categorias=AVENTURA,CULTURA
     */
    @GetMapping("/destacadas")
    public ResponseEntity<List<ActividadDTO>> destacadas(
            @RequestParam(required = false) List<String> categorias,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Si la lista viene vacia la mando como null (matchea el IS NULL de la query)
        List<String> filtro = (categorias == null || categorias.isEmpty())
                ? null : categorias;

        Pageable top = PageRequest.of(0, size);
        List<Actividad> resultado = repository.buscarDestacadas(filtro, top);

        List<ActividadDTO> dtos = resultado.stream()
                .map(ActividadDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
