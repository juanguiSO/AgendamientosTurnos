package com.agendamientos.agendamientosTurnos.dto;

public class CrearMisionDTO {
    private Integer numeroMision; // Número único para la misión
    private String cedulaFuncionario; // Cédula del Funcionario relacionado (recibida desde el frontend)
    private String actividades; // Descripción de las actividades
    private String numeroCaso; // Código del Caso (reemplaza idCaso)
    private Boolean activo; // Estado activo/inactivo
    private Integer idEspecialidad; // ID de la especialidad (opcional)

    // Constructor por defecto
    public CrearMisionDTO() {
    }

    // Constructor con todos los campos
    public CrearMisionDTO(Integer numeroMision, String cedulaFuncionario, String actividades, String numeroCaso, Boolean activo, Integer idEspecialidad) {
        this.numeroMision = numeroMision;
        this.cedulaFuncionario = cedulaFuncionario;
        this.actividades = actividades;
        this.numeroCaso = numeroCaso;
        this.activo = activo;
        this.idEspecialidad = idEspecialidad;
    }

    // Getters y Setters

    public Integer getNumeroMision() {
        return numeroMision;
    }

    public void setNumeroMision(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }

    public String getCedulaFuncionario() {
        return cedulaFuncionario;
    }

    public void setCedulaFuncionario(String cedulaFuncionario) {
        this.cedulaFuncionario = cedulaFuncionario;
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

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }
}