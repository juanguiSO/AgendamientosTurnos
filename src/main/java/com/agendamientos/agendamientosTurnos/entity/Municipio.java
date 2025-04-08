package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

}