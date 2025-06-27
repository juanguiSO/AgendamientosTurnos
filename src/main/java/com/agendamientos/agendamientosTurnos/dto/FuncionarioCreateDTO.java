package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para crear un nuevo funcionario")
public class FuncionarioCreateDTO {

    @Schema(description = "Nombre del funcionario", example = "Carlos")
    private String nombre;

    @Schema(description = "Apellido del funcionario", example = "Pérez")
    private String apellido;

    @Schema(description = "Cédula del funcionario", example = "987654321")
    private String cedula;

    @Schema(description = "Correo electrónico", example = "carlos.perez@example.com")
    private String correo;

    @Schema(description = "Número de teléfono", example = "3123456789")
    private String telefono;

    @Schema(description = "ID de la especialidad asociada", example = "2")
    private Integer idEspecialidad;

    @Schema(description = "ID del grado que atiende", example = "5")
    private Integer idGrado;

    @Schema(description = "ID del rol que se asignará", example = "1")
    private Integer idRol;

    @Schema(description = "Contraseña (solo para usuarios con rol ADMIN)", example = "Segura123!")
    private String contrasena;

    @Schema(description = "Indica si el funcionario está activo (1) o no (0)", example = "1")
    private Integer activo;

    @Schema(description = "ID del cargo asociado", example = "3")
    private Integer idCargo;

public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public Integer getIdGrado() {
        return idGrado;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCedula() {
        return cedula;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

}