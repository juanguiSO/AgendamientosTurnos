package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Data
@AllArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Rol")
    private int idRol;

    @NotBlank(message = "El nombre del rol no puede estar en blanco")
    @Size(max = 45, message = "El nombre del rol no puede tener más de 45 caracteres")
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