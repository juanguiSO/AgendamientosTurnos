package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Rol")
    private int idRol;

    @Column(name = "rol", length = 45, nullable = false)
    private String nombre;

    // Constructor opcional si solo necesitas el nombre:
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdRol() {
        return idRol;
    }
    public Rol() { // Agrega este constructor
    }
}