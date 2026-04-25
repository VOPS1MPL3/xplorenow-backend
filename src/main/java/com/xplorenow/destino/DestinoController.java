package com.xplorenow.destino;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinos")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoRepository repository;

    @GetMapping
    public List<DestinoDTO> listar() {
        return repository.findAll().stream()
                .map(DestinoDTO::desde)
                .toList();
    }
}
