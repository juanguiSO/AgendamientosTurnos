package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mision")
public class Mision {

    @Id
    @Column(name = "numero_mision")
    private Integer numeroMision;


    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario; // Relación con la entidad Funcionario

    @Column(name = "actividades", length = 255)
    private String actividades;

    @ManyToOne
    @JoinColumn(name = "id_caso")
    private Caso caso;

    @Column(name = "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo; // Usamos Boolean para permitir valores nulos si la base de datos lo permite

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public Integer getNumeroMision() {
        return numeroMision;
    }

    public Boolean getActivo() {
        return activo;
    }

    public String getActividades() {
        return actividades;
    }

    public Caso getCaso() {
        return caso;
    }
}