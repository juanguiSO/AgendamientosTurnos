package com.agendamientos.agendamientosTurnos.dto;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull; // Para validaciones, si usas Spring Boot Validation

public class ViajeCreationWithMisionesDTO {

    @NotNull(message = "El tiempo de inicio es obligatorio")
    private LocalDateTime tiempoInicio;

    @NotNull(message = "El tiempo de fin es obligatorio")
    private LocalDateTime tiempoFin;

    private Double distanciaRecorrida; // Puede ser nulo inicialmente

    @NotNull(message = "El ID del estado del viaje es obligatorio")
    private Integer idEstadoViaje;

    @NotNull(message = "El ID del vehículo es obligatorio")
    private Integer idVehiculo;

    // Nueva lista de IDs de misiones
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
