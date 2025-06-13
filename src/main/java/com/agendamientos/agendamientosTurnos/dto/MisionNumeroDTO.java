package com.agendamientos.agendamientosTurnos.dto;

public class MisionNumeroDTO {
    private Integer numeroMision;
    private String nombreFuncionario;
    private String apellidoFuncionario;
    private String especialidadFuncionario; // Asumiendo que la especialidad es un String
    private String numeroCaso;             // Asumiendo que el número de caso es un String (o Integer si lo prefieres)
    private String actividadMision;        // Descripción de la actividad

    // Constructor con todos los campos
    public MisionNumeroDTO(Integer numeroMision, String nombreFuncionario, String apellidoFuncionario,
                           String especialidadFuncionario, String numeroCaso, String actividadMision) {
        this.numeroMision = numeroMision;
        this.nombreFuncionario = nombreFuncionario;
        this.apellidoFuncionario = apellidoFuncionario;
        this.especialidadFuncionario = especialidadFuncionario;
        this.numeroCaso = numeroCaso;
        this.actividadMision = actividadMision;
    }

    // Constructor original (opcional, si aún lo necesitas para casos específicos)
    public MisionNumeroDTO(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }


    // Getters
    public Integer getNumeroMision() {
        return numeroMision;
    }

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public String getApellidoFuncionario() {
        return apellidoFuncionario;
    }

    public String getEspecialidadFuncionario() {
        return especialidadFuncionario;
    }

    public String getNumeroCaso() {
        return numeroCaso;
    }

    public String getActividadMision() {
        return actividadMision;
    }

    // Setters (puedes generarlos automáticamente con tu IDE)
    public void setNumeroMision(Integer numeroMision) {
        this.numeroMision = numeroMision;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }

    public void setApellidoFuncionario(String apellidoFuncionario) {
        this.apellidoFuncionario = apellidoFuncionario;
    }

    public void setEspecialidadFuncionario(String especialidadFuncionario) {
        this.especialidadFuncionario = especialidadFuncionario;
    }

    public void setNumeroCaso(String numeroCaso) {
        this.numeroCaso = numeroCaso;
    }

    public void setActividadMision(String actividadMision) {
        this.actividadMision = actividadMision;
    }
}