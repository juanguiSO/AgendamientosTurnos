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
    private Integer idRol;
    private String contrasena; // Sólo necesaria si es administrador
    private Integer activo;
    private Integer idCargo;
    //private List<Integer> roleIds; // Lista de IDs de roles existentes


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


    //public List<Integer> getRoleIds() {
    //   return roleIds;
    //}

}