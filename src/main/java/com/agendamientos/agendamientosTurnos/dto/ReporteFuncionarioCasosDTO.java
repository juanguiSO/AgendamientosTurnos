package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO que representa un informe de los casos asignados a un funcionario.")
public class ReporteFuncionarioCasosDTO {

    @Schema(description = "Nombre del funcionario.", example = "Andrea")
    private String nombreFuncionario;

    @Schema(description = "Apellido del funcionario.", example = "Gómez")
    private String apellidoFuncionario;

    @Schema(description = "Lista de códigos de casos asignados al funcionario.", example = "[\"012301230123202100520\", \"012301230123202100521\"]")
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