package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotNull(message = "La distancia recorrida no puede ser nula")
    @Min(value = 0, message = "La distancia no puede ser negativa")
    @Column(name = "distancia_recorrida", nullable = false)
    private Double distanciaRecorrida;

    @NotNull(message = "El vehículo asociado al viaje no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Caso> casos = new ArrayList<>();

    @Column(name = "viatico", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean viatico;

    @NotNull(message = "El estado del viaje no puede ser nulo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_viaje", referencedColumnName = "id_estado_viaje", nullable = false)
    private EstadoViaje estadoViaje;

    // Nuevo campo para borrado lógico
    @Column(name = "activo", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activo = true; // Por defecto, un viaje recién creado está activo


    public Viaje() {
    }

    // Constructor actualizado para incluir el campo 'activo'
    public Viaje(LocalDateTime tiempoFin, LocalDateTime tiempoInicio, Double distanciaRecorrida, Vehiculo vehiculo, boolean viatico, EstadoViaje estadoViaje, boolean activo) {
        this.tiempoFin = tiempoFin;
        this.tiempoInicio = tiempoInicio;
        this.distanciaRecorrida = distanciaRecorrida;
        this.vehiculo = vehiculo;
        this.viatico = viatico;
        this.estadoViaje = estadoViaje;
        this.activo = activo;
    }

    // --- Getters y Setters ---
    public Integer getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public LocalDateTime getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(LocalDateTime tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public @NotNull(message = "El tiempo de inicio no puede ser nulo") LocalDateTime getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(@NotNull(message = "El tiempo de inicio no puede ser nulo") LocalDateTime tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public @NotNull(message = "La distancia recorrida no puede ser nula") @Min(value = 0, message = "La distancia no puede ser negativa") Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(@NotNull(message = "La distancia recorrida no puede ser nula") @Min(value = 0, message = "La distancia no puede ser negativa") Double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public @NotNull(message = "El vehículo asociado al viaje no puede ser nulo") Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(@NotNull(message = "El vehículo asociado al viaje no puede ser nulo") Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<Caso> getCasos() {
        return casos;
    }

    public void setCasos(List<Caso> casos) {
        this.casos = casos;
    }

    public void addCaso(Caso caso) {
        if (this.casos == null) {
            this.casos = new ArrayList<>();
        }
        this.casos.add(caso);
        caso.setViaje(this);
    }

    public void removeCaso(Caso caso) {
        if (this.casos != null) {
            this.casos.remove(caso);
            caso.setViaje(null);
        }
    }

    public boolean isViatico() {
        return viatico;
    }

    public void setViatico(boolean viatico) {
        this.viatico = viatico;
    }

    public @NotNull(message = "El estado del viaje no puede ser nulo") EstadoViaje getEstadoViaje() {
        return estadoViaje;
    }

    public void setEstadoViaje(@NotNull(message = "El estado del viaje no puede ser nulo") EstadoViaje estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    public boolean isActivo() { // Getter para el nuevo campo 'activo'
        return activo;
    }

    public void setActivo(boolean activo) { // Setter para el nuevo campo 'activo'
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "idViaje=" + idViaje +
                ", tiempoFin=" + tiempoFin +
                ", tiempoInicio=" + tiempoInicio +
                ", distanciaRecorrida=" + distanciaRecorrida +
                ", vehiculo=" + (vehiculo != null ? vehiculo.getIdVehiculo() : "null") +
                ", viatico=" + viatico +
                ", estadoViaje=" + (estadoViaje != null ? estadoViaje.getEstadoViaje() : "null") +
                ", activo=" + activo + // Incluir en toString
                '}';
    }
}
