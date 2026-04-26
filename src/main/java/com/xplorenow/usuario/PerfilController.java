package com.xplorenow.usuario;

import com.xplorenow.categoria.Categoria;
import com.xplorenow.categoria.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Endpoints del perfil del viajero (Punto 2 del TPO).
 *
 *   GET  /perfil                  -> Datos del usuario logueado
 *   PUT  /perfil                  -> Editar nombre / telefono / foto
 *   PUT  /perfil/preferencias     -> Setear lista de categorias preferidas
 *
 * Todos requieren JWT. El email del usuario se saca del SecurityContext
 * (lo metio el JwtAuthFilter al validar el token).
 */
@RestController
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<PerfilDTO> miPerfil(Authentication auth) {
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .map(PerfilDTO::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<PerfilDTO> actualizar(
            Authentication auth,
            @RequestBody ActualizarPerfilRequest req) {

        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .map(u -> {
                    if (req.getNombre() != null) u.setNombre(req.getNombre());
                    if (req.getTelefono() != null) u.setTelefono(req.getTelefono());
                    if (req.getFotoUrl() != null) u.setFotoUrl(req.getFotoUrl());
                    Usuario guardado = usuarioRepository.save(u);
                    return ResponseEntity.ok(PerfilDTO.desde(guardado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/preferencias")
    public ResponseEntity<PerfilDTO> actualizarPreferencias(
            Authentication auth,
            @RequestBody PreferenciasRequest req) {

        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .map(u -> {
                    List<Long> ids = req.getCategoriaIds() == null
                            ? List.of() : req.getCategoriaIds();
                    Set<Categoria> nuevas = new HashSet<>(
                            categoriaRepository.findAllById(ids));
                    u.setPreferencias(nuevas);
                    Usuario guardado = usuarioRepository.save(u);
                    return ResponseEntity.ok(PerfilDTO.desde(guardado));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
