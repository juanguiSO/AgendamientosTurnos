package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa una misión por número, incluyendo detalles del funcionario, caso y ubicación.")
public class MisionNumeroDTO {

    @Schema(
            description = "Número único de la misión asignada, compuesto por 9 dígitos.",
            example = "202500476",
            minimum = "100000000", maximum = "999999999"
    )
    private Integer numeroMision;

    @Schema(
            description = "Nombre del funcionario asignado a la misión.",
            example = "Carlos"
    )
    private String nombreFuncionario;

    @Schema(
            description = "Apellido del funcionario asignado.",
            example = "Pérez"
    )
    private String apellidoFuncionario;

    @Schema(
            description = "Especialidad del funcionario en la misión.",
            example = "Derecho Penal"
    )
    private String especialidadFuncionario;

    @Schema(
            description = "Número del caso judicial asociado a la misión, con 21 dígitos.",
            example = "012301230123202100520",
            pattern = "\\d{21}"
    )
    private String numeroCaso;

    @Schema(
            description = "Descripción de la actividad a realizar durante la misión.",
            example = "Entrevista a testigos y toma de fotografías"
    )
    private String actividadMision;

    @Schema(
            description = "Nombre del municipio donde se realiza la misión.",
            example = "Medellín"
    )
    private String nombreMunicipio;

    // Constructor con todos los campos
    public MisionNumeroDTO(Integer numeroMision, String nombreFuncionario, String apellidoFuncionario,
                           String especialidadFuncionario, String numeroCaso, String actividadMision, String nombreMunicipio) {
        this.numeroMision = numeroMision;
        this.nombreFuncionario = nombreFuncionario;
        this.apellidoFuncionario = apellidoFuncionario;
        this.especialidadFuncionario = especialidadFuncionario;
        this.numeroCaso = numeroCaso;
        this.actividadMision = actividadMision;
        this.nombreMunicipio =nombreMunicipio;
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

    public String getNombreMunicipio() {
        return nombreMunicipio;
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

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }
}