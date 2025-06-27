package com.agendamientos.agendamientosTurnos.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa una misión asignada a un funcionario con detalles del caso y la especialidad.")
public class MisionDTO {

    @Schema(
            description = "Número único de la misión asignada, compuesto por 9 dígitos.",
            example = "202500476",
            minimum = "100000000", maximum = "999999999"
    )
    private Integer numeroMision;

    @Schema(description = "ID del funcionario al que se asignó la misión", example = "12")
    private Integer idFuncionario;

    @Schema(description = "Nombre del funcionario", example = "Carlos")
    private String nombreFuncionario;

    @Schema(description = "Apellido del funcionario", example = "Pérez")
    private String apellidoFuncionario;

    @Schema(description = "Descripción de las actividades asignadas en la misión", example = "Inspección ocular y toma de testimonios")
    private String actividades;

    @Schema(description = "Código del caso judicial asociado a la misión, numero de 21 digitos", example = "012301230123202100520")
    private String numeroCaso;

    @Schema(description = "ID interno del caso", example = "20")
    private Integer idCaso;

    @Schema(description = "Indica si la misión está activa", example = "true")
    private Boolean activo;

    @Schema(description = "ID de la especialidad del funcionario", example = "3")
    private Integer idEspecialidad;

    @Schema(description = "Nombre de la especialidad asociada al funcionario", example = "Derecho Penal")
    private String especialidad;


    public MisionDTO() {
    }

    public MisionDTO(Integer numeroMision, Integer idFuncionario, String nombreFuncionario, String apellidoFuncionario, String actividades, String numeroCaso, Integer idCaso, Boolean activo,Integer idEspecialidad,  String especialidad) {
        this.numeroMision = numeroMision;
        this.idFuncionario = idFuncionario;
        this.nombreFuncionario = nombreFuncionario;
        this.apellidoFuncionario = apellidoFuncionario;
        this.actividades = actividades;
        this.numeroCaso = numeroCaso;
        this.idCaso = idCaso;
        this.activo = activo;
        this.idEspecialidad= idEspecialidad;
        this.especialidad= especialidad;
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

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }
}