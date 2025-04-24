package com.agendamientos.agendamientosTurnos.dto;

public class CrearMisionDTO {
    private Integer numeroMision; // Número único para la misión
    private Integer idFuncionario; // ID del Funcionario relacionado
    private String actividades; // Descripción de las actividades
    private Integer idCaso; // ID del Caso relacionado
    private Boolean activo; // Estado activo/inactivo

    // Constructor por defecto (requerido para algunas frameworks)
    public CrearMisionDTO() {
    }

    // Constructor con todos los campos
    public CrearMisionDTO(Integer numeroMision, Integer idFuncionario, String actividades, Integer idCaso, Boolean activo) {
        this.numeroMision = numeroMision;
        this.idFuncionario = idFuncionario;
        this.actividades = actividades;
        this.idCaso = idCaso;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getNumeroMision() {
        return numeroMision;
    }

    public void setNumeroMision(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public Integer getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Integer idCaso) {
        this.idCaso = idCaso;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}