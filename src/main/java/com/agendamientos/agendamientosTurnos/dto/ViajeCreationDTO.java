package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de entrada para la creación de un nuevo viaje.
 * Contiene los datos necesarios para registrar el viaje y los IDs de los casos asociados.
 */
@Schema(description = "DTO utilizado para la creación de un nuevo viaje.")
public class ViajeCreationDTO {

    @Schema(description = "Fecha y hora de inicio del viaje", example = "2025-06-17T08:00:00")
    private LocalDateTime tiempoInicio;

    @Schema(description = "Fecha y hora de finalización del viaje", example = "2025-06-17T12:00:00")
    private LocalDateTime tiempoFin;

    @Schema(description = "Distancia total recorrida en kilómetros", example = "42.5")
    private double distanciaRecorrida;

    @Schema(description = "ID del estado del viaje", example = "1")
    private Integer idEstadoViaje;

    @Schema(description = "ID del vehículo utilizado en el viaje", example = "5")
    private Integer idVehiculo;

    @Schema(description = "Lista de IDs de los casos a asociar al viaje", example = "[101, 102, 103]")
    private List<Integer> idCasos; // Lista de IDs de casos a intentar asignar al viaje

    // Constructor vacío (necesario para la deserialización JSON por frameworks como Spring)
    public ViajeCreationDTO() {}

    // Getters y Setters para todas las propiedades

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

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
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

    public List<Integer> getIdCasos() {
        return idCasos;
    }

    public void setIdCasos(List<Integer> idCasos) {
        this.idCasos = idCasos;
    }
}
