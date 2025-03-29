package com.agendamientos.agendamientosTurnos.dto;

import lombok.Data;

import java.util.List;

@Data
public class FuncionarioCreateDTO {

    private String apellido;
    private String cedula;
    private String correo;
    private String nombre;
    private String telefono;
    private Integer idEspecialidad;
    private Integer idGrado;
    private String contrasena; // Sólo necesaria si es administrador
    private Boolean activo;
    private List<Integer> roleIds; // Lista de IDs de roles existentes


    public Integer getIdGrado() {
        return idGrado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
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

    public List<Integer> getRoleIds() {
        return roleIds;
    }

}