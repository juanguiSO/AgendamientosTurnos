package com.agendamientos.agendamientosTurnos.dto;

public class MisionDTO {
    private Integer numeroMision;
    private Integer idFuncionario;
    private String nombreFuncionario;
    private String apellidoFuncionario;
    private String actividades;
    private String numeroCaso;
    private Integer idCaso; // Nuevo campo para el ID del caso
    private Boolean activo;

    public MisionDTO() {
    }

    public MisionDTO(Integer numeroMision, Integer idFuncionario, String nombreFuncionario, String apellidoFuncionario, String actividades, String numeroCaso, Integer idCaso, Boolean activo) {
        this.numeroMision = numeroMision;
        this.idFuncionario = idFuncionario;
        this.nombreFuncionario = nombreFuncionario;
        this.apellidoFuncionario = apellidoFuncionario;
        this.actividades = actividades;
        this.numeroCaso = numeroCaso;
        this.idCaso = idCaso;
        this.activo = activo;
    }

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

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }

    public String getApellidoFuncionario() {
        return apellidoFuncionario;
    }

    public void setApellidoFuncionario(String apellidoFuncionario) {
        this.apellidoFuncionario = apellidoFuncionario;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getNumeroCaso() {
        return numeroCaso;
    }

    public void setNumeroCaso(String numeroCaso) {
        this.numeroCaso = numeroCaso;
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