package com.xplorenow.notificacion;

/**
 * Tipos de novedad que puede recibir un viajero (Punto 12 del TPO).
 */
public enum TipoNovedad {
    /** Recordatorio automático: la actividad es en menos de 24hs. */
    RECORDATORIO_24H,
    /** La operadora canceló la actividad/reserva. */
    CANCELACION,
    /** La operadora reprogramó el horario de la actividad. */
    REPROGRAMACION
}
