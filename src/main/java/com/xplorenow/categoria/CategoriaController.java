package com.xplorenow.categoria;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository repository;

    @GetMapping
    public List<CategoriaDTO> listar() {
        return repository.findAll().stream()
                .map(CategoriaDTO::desde)
                .toList();
    }
}
