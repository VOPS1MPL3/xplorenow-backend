package com.xplorenow.favorito;

import com.xplorenow.actividad.Actividad;
import com.xplorenow.actividad.ActividadRepository;
import com.xplorenow.usuario.Usuario;
import com.xplorenow.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de favoritos (Punto 7 del TPO).
 *
 *   POST   /favoritos/{actividadId}   -> Marcar
 *   DELETE /favoritos/{actividadId}   -> Desmarcar
 *   GET    /favoritos                 -> Mis favoritos con flag de novedad
 */
@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;

    @PostMapping("/{actividadId}")
    public ResponseEntity<FavoritoDTO> marcar(
            Authentication auth,
            @PathVariable Long actividadId) {

        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Actividad a = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));

        FavoritoId id = new FavoritoId(u.getId(), a.getId());

        // Si ya esta marcada, devolvemos la existente (idempotente)
        Favorito existente = favoritoRepository.findById(id).orElse(null);
        if (existente != null) {
            return ResponseEntity.ok(FavoritoDTO.desde(existente));
        }

        Favorito f = Favorito.builder()
                .id(id)
                .usuario(u)
                .actividad(a)
                .precioAlMarcar(a.getPrecio())
                .cuposAlMarcar(a.getCuposDisponibles())
                .build();
        Favorito guardado = favoritoRepository.save(f);
        return ResponseEntity.ok(FavoritoDTO.desde(guardado));
    }

    @DeleteMapping("/{actividadId}")
    public ResponseEntity<Void> desmarcar(
            Authentication auth,
            @PathVariable Long actividadId) {

        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        FavoritoId id = new FavoritoId(u.getId(), actividadId);
        if (!favoritoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        favoritoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoritoDTO>> misFavoritos(Authentication auth) {
        Usuario u = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        List<FavoritoDTO> dtos = favoritoRepository
                .findByUsuarioOrderByMarcadaEnDesc(u).stream()
                .map(FavoritoDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
