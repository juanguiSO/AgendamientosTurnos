package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa un viaje con su identificador y duración.")
public class ViajeDTO {

    @Schema(description = "Identificador único del viaje.", example = "1023")
    private Integer idViaje;

    @Schema(description = "Fecha y hora de inicio del viaje en formato ISO 8601.", example = "2025-06-17T08:00:00")
    private String tiempoInicio;

    @Schema(description = "Fecha y hora de finalización del viaje en formato ISO 8601.", example = "2025-06-17T17:30:00")
    private String tiempoFin;


    public Integer getIdViaje() {
        return idViaje;
    }
}