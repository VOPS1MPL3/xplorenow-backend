package com.xplorenow.actividad;

import com.xplorenow.categoria.Categoria;
import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Endpoints del Catalogo de Actividades (Punto 3 del TPO).
 *
 *   GET /actividades                 -> Listado paginado con filtros
 *   GET /actividades/{id}            -> Detalle completo
 *   GET /actividades/destacadas      -> Recomendadas segun preferencias del perfil
 */
@RestController
@RequestMapping("/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadRepository repository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Listado paginado con filtros combinados. Todos los filtros son opcionales
     * y se resuelven en una sola query JPQL (ver ActividadRepository): cada
     * parametro que llega null se "apaga" con un IS NULL, evitando tener que
     * escribir una query por combinacion posible.
     *
     * Ejemplo: GET /actividades?destinoId=1&categoriaId=3&precioMax=15000&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<ActividadDTO>> listar(
            @RequestParam(required = false) Long destinoId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
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
     * Seccion "Destacadas para vos" (Punto 3 del TPO): recomendaciones segun
     * las preferencias de viaje que el usuario guardo en su perfil.
     *
     * Las preferencias NO llegan por query param: se leen del usuario que el
     * JWT identifica. El servidor ya sabe quien es el que pide, no tiene
     * sentido que el cliente le informe sus propios datos (y ademas seria
     * manipulable desde afuera).
     *
     * Si el usuario todavia no cargo preferencias, el filtro queda en null y
     * la query devuelve todas las actividades ordenadas por cupos disponibles.
     *
     *   GET /actividades/destacadas
     */
    @GetMapping("/destacadas")
    public ResponseEntity<List<ActividadDTO>> destacadas(
            Authentication auth,
            @RequestParam(defaultValue = "10") int size
    ) {
        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<String> filtro = u.getPreferencias().isEmpty()
                ? null
                : u.getPreferencias().stream()
                    .map(Categoria::getCodigo)
                    .toList();

        Pageable top = PageRequest.of(0, size);
        List<ActividadDTO> dtos = repository.buscarDestacadas(filtro, top).stream()
                .map(ActividadDTO::desde)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}