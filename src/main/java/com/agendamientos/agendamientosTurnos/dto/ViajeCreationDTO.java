package com.agendamientos.agendamientosTurnos.dto;


import java.time.LocalDateTime;
import java.util.List;

public class ViajeCreationDTO {
    private Integer idVehiculo;
    private Integer idEstadoViaje;
    private Boolean viatico;
    private Double distanciaRecorrida;
    private LocalDateTime tiempoInicio;
    private LocalDateTime tiempoFin;
    private Integer estado;
    private List<Integer> idCasos; // Lista de IDs de casos a asignar

    // Getters y setters
    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Integer getIdEstadoViaje() {
        return idEstadoViaje;
    }

    public void setIdEstadoViaje(Integer idEstadoViaje) {
        this.idEstadoViaje = idEstadoViaje;
    }

    public Boolean getViatico() {
        return viatico;
    }

    public void setViatico(Boolean viatico) {
        this.viatico = viatico;
    }

    public Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(Double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<Integer> getIdCasos() {
        return idCasos;
    }

    public void setIdCasos(List<Integer> idCasos) {
        this.idCasos = idCasos;
    }
}