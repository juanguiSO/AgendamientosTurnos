package com.agendamientos.agendamientosTurnos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "mision")
public class Mision {

    @Id
    @Column(name = "numero_mision")
    private Integer numeroMision;


    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    @JsonIgnore
    private Funcionario funcionario; // Relación con la entidad Funcionario

    @Column(name = "actividades", length = 255)
    private String actividades;

    @ManyToOne
    @JoinColumn(name = "id_caso")
    @JsonIgnore
    private Caso caso;

    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;

    @Column(name = "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo; // Usamos Boolean para permitir valores nulos si la base de datos lo permite

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setCaso(Caso caso) {
        this.caso = caso;
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

    public void setNumeroMision(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }


}