package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa los datos públicos de un funcionario")
public class FuncionarioDTO {

    @Schema(description = "Cédula del funcionario", example = "12345678")
    private String cedula;

    @Schema(description = "Nombre del funcionario", example = "María")
    private String nombre;

    @Schema(description = "Apellido del funcionario", example = "Gómez")
    private String apellido;

    @Schema(description = "Correo electrónico del funcionario", example = "maria.gomez@example.com")
    private String correo;

    @Schema(description = "Teléfono de contacto del funcionario", example = "3114567890")
    private String telefono;

    @Schema(description = "Especialidad profesional del funcionario", example = "Investigador")
    private String especialidad;

    @Schema(description = "Grado que atiende el funcionario", example = "14")
    private String grado;

    @Schema(description = "Cargo del funcionario", example = "Auxiliar Administrativo")
    private String cargo;

    @Schema(description = "Indica si el funcionario está activo (1) o inactivo (0)", example = "1")
    private Integer activo;

    @Schema(description = "Rol asignado en el sistema", example = "ADMIN")
    private String rol;


    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGrado() {
        return grado;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public String getRol() { return rol; }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setRol(String rol) { this.rol = rol; }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }



}