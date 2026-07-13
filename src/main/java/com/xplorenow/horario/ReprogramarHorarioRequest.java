package com.xplorenow.horario;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReprogramarHorarioRequest {
    private LocalDate fecha;
    private LocalTime hora;
}
