package com.agendamientos.agendamientosTurnos.dto;



import lombok.Data;

@Data
public class FuncionarioDTO {
    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String especialidad; // Nombre de la especialidad
    private String grado; // Nombre del grado
    private String cargo; // Nombre del cargo
    private Integer activo;


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