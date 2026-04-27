package com.xplorenow.noticia;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de noticias (Punto 9 del TPO).
 *
 *   GET /noticias        -> Listado (mas nuevas primero)
 *   GET /noticias/{id}   -> Detalle
 *
 * Publicos como /actividades.
 */
@RestController
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaRepository repository;

    @GetMapping
    public List<NoticiaDTO> listar() {
        return repository.findAllByOrderByPublicadaEnDesc().stream()
                .map(NoticiaDTO::desde)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticiaDTO> detalle(@PathVariable Long id) {
        return repository.findById(id)
                .map(NoticiaDTO::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
