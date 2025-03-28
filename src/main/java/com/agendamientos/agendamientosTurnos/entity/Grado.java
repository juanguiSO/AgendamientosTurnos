package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
@Entity
@Table(name= "grado")
public class Grado {

    @Id
    @Column(name = "id_grado", nullable = false)
    private int idGrado;

    @Column(name = "grado", nullable = false, length = 100)
    private String Grado;
    @Column(name = "nombre_grado", nullable = false, length = 100)
    private String nombreDelGrado;

    public int getIdGrado() {
        return idGrado;
    }

    public String getGrado() {
        return Grado;
    }

    public String getNombreDelGrado() {
        return nombreDelGrado;
    }
}
