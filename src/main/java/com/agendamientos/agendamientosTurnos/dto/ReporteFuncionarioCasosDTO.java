package com.agendamientos.agendamientosTurnos.dto;

import java.util.List;

public class ReporteFuncionarioCasosDTO {
    private String nombreFuncionario;
    private String apellidoFuncionario;
    private List<String> casos;

    public ReporteFuncionarioCasosDTO(String nombre, String apellido, List<String> codigosCasos) {
        this.nombreFuncionario = nombre;
        this.apellidoFuncionario = apellido;
        this.casos = codigosCasos;
    }

    public String getApellidoFuncionario() {
        return apellidoFuncionario;
    }

    public String getNombreFuncionario() {
        return nombreFuncionario;
    }

    public List<String> getCasos() {
        return casos;
    }

    public void setApellidoFuncionario(String apellidoFuncionario) {
        this.apellidoFuncionario = apellidoFuncionario;
    }

    public void setNombreFuncionario(String nombreFuncionario) {
        this.nombreFuncionario = nombreFuncionario;
    }

    public void setCasos(List<String> casos) {
        this.casos = casos;
    }


}