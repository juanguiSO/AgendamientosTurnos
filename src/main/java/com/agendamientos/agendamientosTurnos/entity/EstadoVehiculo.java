package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity

@Table(name= "estado_vehiculo")
public class EstadoVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_vehiculo")
    private Integer idEstadoVehiculo;

    @Column(name = "estado_vehiculo", length = 45, nullable = false)
    private String estadoVehiculo;

    public void setEstadoVehiculo(String estadoVehiculo) {
        this.estadoVehiculo = estadoVehiculo;
    }

    public void setIdEstadoVehiculo(Integer idEstadoVehiculo) {
        this.idEstadoVehiculo = idEstadoVehiculo;
    }

    public Integer getIdEstadoVehiculo() {
        return idEstadoVehiculo;
    }

    public String getEstadoVehiculo() {
        return estadoVehiculo;
    }
}
