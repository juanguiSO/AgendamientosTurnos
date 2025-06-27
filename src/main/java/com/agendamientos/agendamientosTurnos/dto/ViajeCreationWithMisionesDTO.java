package com.agendamientos.agendamientosTurnos.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para la creación de un viaje junto con las misiones asociadas.")
public class ViajeCreationWithMisionesDTO {

    @NotNull(message = "El tiempo de inicio es obligatorio")
    @Schema(description = "Fecha y hora de inicio del viaje (formato ISO 8601)", example = "2025-06-17T08:00:00")
    private LocalDateTime tiempoInicio;

    @NotNull(message = "El tiempo de fin es obligatorio")
    @Schema(description = "Fecha y hora de fin del viaje (formato ISO 8601)", example = "2025-06-17T17:00:00")
    private LocalDateTime tiempoFin;

    @Schema(description = "Distancia recorrida durante el viaje en kilómetros (opcional)", example = "120.5")
    private Double distanciaRecorrida;

    @NotNull(message = "El ID del estado del viaje es obligatorio")
    @Schema(description = "ID del estado actual del viaje", example = "2")
    private Integer idEstadoViaje;

    @NotNull(message = "El ID del vehículo es obligatorio")
    @Schema(description = "ID del vehículo asignado para el viaje", example = "15")
    private Integer idVehiculo;

    @Schema(description = "Lista de IDs de misiones asociadas al viaje", example = "[101, 102, 103]")
    private List<Integer> idMisiones;

    // Getters y Setters
    public LocalDateTime getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(LocalDateTime tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public LocalDateTime getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(LocalDateTime tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(Double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public Integer getIdEstadoViaje() {
        return idEstadoViaje;
    }

    public void setIdEstadoViaje(Integer idEstadoViaje) {
        this.idEstadoViaje = idEstadoViaje;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public List<Integer> getIdMisiones() {
        return idMisiones;
    }

    public void setIdMisiones(List<Integer> idMisiones) {
        this.idMisiones = idMisiones;
    }
}
