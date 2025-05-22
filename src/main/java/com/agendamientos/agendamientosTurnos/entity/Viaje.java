package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min; // Para validar que la distancia no sea negativa
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@Entity
@Table(name = "viaje")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Integer idViaje;



    @Column(name = "tiempo_fin")
    private LocalDateTime tiempoFin;

    @NotNull(message = "El tiempo de inicio no puede ser nulo")
    @Column(name = "tiempo_inicio", nullable = false)
    private LocalDateTime tiempoInicio;

    // NUEVO CAMPO: Distancia recorrida
    @NotNull(message = "La distancia recorrida no puede ser nula")
    @Min(value = 0, message = "La distancia no puede ser negativa")
    @Column(name = "distancia_recorrida", nullable = false)
    private Double distanciaRecorrida; // O Integer si solo manejas kilómetros enteros

    @NotNull(message = "El vehículo asociado al viaje no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "viaje")
    private List<Caso> casos;

    // El campo 'viatico' ahora será calculado por la lógica de negocio
    @Column(name = "viatico", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean viatico;

    @NotNull(message = "El estado del viaje no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_viaje", referencedColumnName = "id_estado_viaje", nullable = false)
    private EstadoViaje estadoViaje;

    // Constructores
    public Viaje() {
    }

    // Constructor actualizado para incluir distanciaRecorrida
    public Viaje(LocalDateTime tiempoFin, LocalDateTime tiempoInicio, Double distanciaRecorrida, Vehiculo vehiculo, boolean viatico, EstadoViaje estadoViaje) {

        this.tiempoFin = tiempoFin;
        this.tiempoInicio = tiempoInicio;
        this.distanciaRecorrida = distanciaRecorrida;
        this.vehiculo = vehiculo;
        this.viatico = viatico;
        this.estadoViaje = estadoViaje;
    }

    public @NotNull(message = "El estado del viaje no puede ser nulo") EstadoViaje getEstadoViaje() {
        return estadoViaje;
    }

    public Integer getIdViaje() {
        return idViaje;
    }



    public @NotNull(message = "El vehículo asociado al viaje no puede ser nulo") Vehiculo getVehiculo() {
        return vehiculo;
    }

    public @NotNull(message = "El tiempo de inicio no puede ser nulo") LocalDateTime getTiempoInicio() {
        return tiempoInicio;
    }

    public LocalDateTime getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoInicio(@NotNull(message = "El tiempo de inicio no puede ser nulo") LocalDateTime tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }



    public void setTiempoFin(LocalDateTime tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public void setVehiculo(@NotNull(message = "El vehículo asociado al viaje no puede ser nulo") Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setEstadoViaje(@NotNull(message = "El estado del viaje no puede ser nulo") EstadoViaje estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    public void setViatico(boolean viatico) {
        this.viatico = viatico;
    }


    public void setDistanciaRecorrida(Double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "idViaje=" + idViaje +
                ", tiempoFin=" + tiempoFin +
                ", tiempoInicio=" + tiempoInicio +
                ", distanciaRecorrida=" + distanciaRecorrida + // Incluir en toString
                ", vehiculo=" + (vehiculo != null ? vehiculo.getIdVehiculo() : "null") +
                ", viatico=" + viatico +
                ", estadoViaje=" + (estadoViaje != null ? estadoViaje.getEstadoViaje() : "null") +
                '}';
    }
}