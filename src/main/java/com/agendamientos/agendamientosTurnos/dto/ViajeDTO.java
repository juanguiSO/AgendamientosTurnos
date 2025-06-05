package com.agendamientos.agendamientosTurnos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {
    private Integer idViaje;
    private String tiempoInicio;
    private String tiempoFin;


    public Integer getIdViaje() {
        return idViaje;
    }
}