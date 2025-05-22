package com.agendamientos.agendamientosTurnos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehiculo;

    @NotBlank(message = "La marca no puede estar en blanco")
    @Size(max = 45, message = "La marca no puede tener más de 45 caracteres")
    @Column(name = "marca", length = 45)
    private String marca;

    @NotBlank(message = "El modelo no puede estar en blanco")
    @Size(max = 45, message = "El modelo no puede tener más de 45 caracteres")
    @Column(name = "modelo", length = 45)
    private String modelo;

    @NotNull(message = "El número de asientos no puede ser nulo")
    @Min(value = 2, message = "El número de asiento debe ser como mínimo 2")
    @Max(value = 4, message = "El número de asiento debe ser como máximo 4")
    @Column(name = "numero_asiento")
    private Integer numeroAsiento;

    @NotBlank(message = "La placa no puede estar en blanco")
    @Size(max = 45, message = "La placa no puede tener más de 45 caracteres")
    @Column(name = "placa", length = 45)
    private String placa;

    @ManyToOne
    @JoinColumn(name = "id_estado_vehiculo")
    private EstadoVehiculo estadoVehiculo;

    @Column(name = "activo") // Nuevo campo para el borrado lógico
    private boolean activo;

    // Constructores

    public Vehiculo() {
    }

    public Vehiculo(String marca, String modelo, Integer numeroAsiento, String placa, EstadoVehiculo estadoVehiculo) {
        this.marca = marca;
        this.modelo = modelo;
        this.numeroAsiento = numeroAsiento;
        this.placa = placa;
        this.estadoVehiculo = estadoVehiculo;
    }

    // Getters y Setters

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public EstadoVehiculo getEstadoVehiculo() {
        return estadoVehiculo;
    }

    public void setEstadoVehiculo(EstadoVehiculo estadoVehiculo) {
        this.estadoVehiculo = estadoVehiculo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    @Override
    public String toString() {
        return "Vehiculo{" +
                "idVehiculo=" + idVehiculo +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", numeroAsiento=" + numeroAsiento +
                ", placa='" + placa + '\'' +
                ", estadoVehiculo=" + (estadoVehiculo != null ? estadoVehiculo.getIdEstadoVehiculo() : null) +
                '}';
    }
}