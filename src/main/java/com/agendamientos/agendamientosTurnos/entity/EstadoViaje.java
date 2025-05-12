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
@Table(name = "estado_viaje") // Nombre de la tabla en la base de datos
public class EstadoViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_viaje") // Nombre de la columna en la base de datos
    private Integer idEstadoViaje;

    @NotBlank(message = "El estado del viaje no puede estar en blanco")
    @Size(max = 255, message = "El estado del viaje no puede tener más de 255 caracteres")
    @Column(name = "estado_viaje", length = 255, nullable = false) // Nombre de la columna en la base de datos y sus restricciones
    private String estadoViaje;


    public EstadoViaje() {
    }

    public EstadoViaje(String estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    // Getters y Setters (ya están cubiertos por Lombok @Getter y @Setter,
    // pero los incluyo explícitamente si no estás usando Lombok o para claridad)

    public Integer getIdEstadoViaje() {
        return idEstadoViaje;
    }

    public void setIdEstadoViaje(Integer idEstadoViaje) {
        this.idEstadoViaje = idEstadoViaje;
    }

    public String getEstadoViaje() {
        return estadoViaje;
    }

    public void setEstadoViaje(String estadoViaje) {
        this.estadoViaje = estadoViaje;
    }

    @Override
    public String toString() {
        return "EstadoViaje{" +
                "idEstadoViaje=" + idEstadoViaje +
                ", estadoViaje='" + estadoViaje + '\'' +
                '}';
    }
}