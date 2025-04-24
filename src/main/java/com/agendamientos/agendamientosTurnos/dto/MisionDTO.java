package com.agendamientos.agendamientosTurnos.dto;

public class MisionDTO {
    private Integer numeroMision;
    //private Integer idFuncionario; // Cambiado a ID
    private String nombreFuncionario; // Puedes mantenerlo para mostrar, pero usa idFuncionario para la relación
    private String apellidoFuncionario; // Puedes mantenerlo para mostrar
    private String actividades;
    private String numeroCaso;
    private Boolean activo;

    // Constructor, getters y setters
    public MisionDTO() {
    }

    public MisionDTO(Integer numeroMision, String nombreFuncionario, String apellidoFuncionario, String actividades, String numeroCaso, Boolean activo) {
        this.numeroMision = numeroMision;
        this.nombreFuncionario = nombreFuncionario;
        this.apellidoFuncionario = apellidoFuncionario;
        this.actividades = actividades;
        this.numeroCaso = numeroCaso;
        this.activo = activo;
    }

    // Getters y setters para todos los campos, incluyendo idFuncionario
    public Integer getNumeroMision() {
        return numeroMision;
    }

    public void setNumeroMision(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }
/**
    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
*/
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}