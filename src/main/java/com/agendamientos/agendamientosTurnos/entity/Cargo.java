package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer idCargo;

    @Column(name = "nombre_cargo", length = 225, nullable = false, unique = true)
    private String nombreCargo;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activo = true;

    public Integer getIdCargo() {
        return idCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean getActivo() {
        return activo;
    }


}