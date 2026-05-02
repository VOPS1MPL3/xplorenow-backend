package com.xplorenow.actividad;

import com.xplorenow.categoria.Categoria;
import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 *   GET /actividades/destacadas      -> Recomendadas segun preferencias del usuario logueado,
 *                                       o todas si no hay sesion activa.
 */
@RestController
@RequestMapping("/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadRepository repository;
    private final UsuarioRepository usuarioRepository;

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
     * Destacadas / recomendadas.
     *
     * - Si el usuario esta logueado Y tiene preferencias configuradas:
     *   filtra por los codigos de sus categorias favoritas.
     * - Si no esta logueado O no tiene preferencias: devuelve todas
     *   ordenadas por cupos descendente (comportamiento anterior).
     *
     * El filtrado se hace aca en el backend — la app NO necesita
     * pasar ?categorias=... manualmente.
     *
     * Ejemplo logueado: GET /actividades/destacadas
     *   → Spring Security ya cargo el email en el SecurityContext via JwtAuthFilter
     *   → leemos las preferencias del usuario y filtramos
     *
     * Ejemplo anonimo: GET /actividades/destacadas
     *   → authentication == null → devuelve todas
     */
    @GetMapping("/destacadas")
    public ResponseEntity<List<ActividadDTO>> destacadas(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<String> codigos = null;

        // Solo intentamos leer preferencias si hay un usuario autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            codigos = usuarioRepository.findByEmail(email)
                    .map(Usuario::getPreferencias)
                    .filter(prefs -> !prefs.isEmpty())
                    .map(prefs -> prefs.stream()
                            .map(Categoria::getCodigo)
                            .toList())
                    .orElse(null);
        }

        Pageable top = PageRequest.of(0, size);
        List<Actividad> resultado = repository.buscarDestacadas(codigos, top);

        List<ActividadDTO> dtos = resultado.stream()
                .map(ActividadDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}