package com.agendamientos.agendamientosTurnos.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO utilizado para crear una nueva misión.")
public class CrearMisionDTO {
    @Schema(
            description = "Número único de la misión asignada, compuesto por 9 dígitos.",
            example = "202500476",
            minimum = "100000000", maximum = "999999999"
    )
    private Integer numeroMision;

    @Schema(description = "Cédula del funcionario asignado a la misión", example = "1234567890")
    private String cedulaFuncionario;

    @Schema(description = "Descripción de las actividades que realizará el funcionario", example = "Visita al centro penitenciario y recolección de evidencias.")
    private String actividades;

    @Schema(description = "Código del caso judicial relacionado con la misión", example = "012301230123202100520")
    private String numeroCaso;

    @Schema(description = "Estado de la misión (activa o no)", example = "true")
    private Boolean activo;

    @Schema(description = "ID de la especialidad asociada al funcionario (puede ser opcional)", example = "3")
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