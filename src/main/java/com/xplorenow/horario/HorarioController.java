package com.xplorenow.horario;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Horarios disponibles de una actividad para una fecha (Punto 4 del TPO).
 *
 *   GET /actividades/{id}/horarios?fecha=2026-05-15
 */
@RestController
@RequestMapping("/actividades")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioRepository repository;

    @GetMapping("/{actividadId}/horarios")
    public ResponseEntity<List<HorarioDTO>> horariosDe(
            @PathVariable Long actividadId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<HorarioDTO> dtos = repository
                .findByActividadIdAndFechaOrderByHoraAsc(actividadId, fecha)
                .stream()
                .map(HorarioDTO::desde)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{actividadId}/horarios/disponibles")
        public ResponseEntity<List<HorarioDTO>> todosLosHorarios(
        @PathVariable Long actividadId) {

        List<HorarioDTO> dtos = repository
            .findByActividadIdAndCuposRestantesGreaterThanOrderByFechaAscHoraAsc(
                    actividadId, 0)
            .stream()
            .map(HorarioDTO::desde)
            .toList();
        return ResponseEntity.ok(dtos);
    }
}
