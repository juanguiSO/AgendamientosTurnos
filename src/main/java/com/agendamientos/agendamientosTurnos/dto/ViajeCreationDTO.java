package com.agendamientos.agendamientosTurnos.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de entrada para la creación de un nuevo viaje.
 * Contiene los datos necesarios para el viaje y los IDs de los casos a asociar.
 */
public class ViajeCreationDTO {
    private LocalDateTime tiempoInicio;
    private LocalDateTime tiempoFin;
    private double distanciaRecorrida;
    private Integer idEstadoViaje;
    private Integer idVehiculo;
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
