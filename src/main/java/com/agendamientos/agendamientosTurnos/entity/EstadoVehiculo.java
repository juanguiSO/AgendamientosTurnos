package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El estado del vehículo no puede estar en blanco")
    @Size(max = 45, message = "El estado del vehículo no puede tener más de 45 caracteres")
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