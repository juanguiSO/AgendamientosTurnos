package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamentos")
    private Integer idDepartamentos;

    @NotBlank(message = "El nombre del departamento no puede estar en blanco")
    @Size(max = 255, message = "El nombre del departamento no puede tener más de 255 caracteres")
    @Column(name = "departamento", length = 255)
    private String departamento;

    @OneToMany(mappedBy = "departamento")
    private List<Municipio> municipios;

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}