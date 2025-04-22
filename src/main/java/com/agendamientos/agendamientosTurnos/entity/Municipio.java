package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.agendamientos.agendamientosTurnos.entity.Departamento;

@Getter
@Setter
@Data
@Entity
@Table(name = "municipio")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_municipio")
    private Integer idMunicipio;

    @NotBlank(message = "El nombre del municipio no puede estar en blanco")
    @Size(max = 255, message = "El nombre del municipio no puede tener más de 255 caracteres")
    @Column(name = "municipio", length = 255)
    private String municipio;

    @ManyToOne
    @JoinColumn(name = "id_departamentos", referencedColumnName = "id_departamentos", insertable = false, updatable = false)
    private Departamento departamento;

    // **Agrega este campo para mapear la columna id_departamentos**
    @Column(name = "id_departamentos")
    private Integer idDepartamento;


    public String getMunicipio() {
        return municipio;

    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

}