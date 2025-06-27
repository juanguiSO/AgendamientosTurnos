package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

    @Schema(description = "Representación simplificada de un caso judicial.")
    public class CasoDTO {

        @Schema(description = "Identificador único del caso", example = "1")
        private Integer idCaso;

        @Schema(description = "Código único del caso, numero de 21 digitos", example = "012301230123202100520")
        private String codigoCaso;

        @Schema(description = "ID del delito asociado al caso", example = "7")
        private Integer idDelito;

        @Schema(description = "Nombre del defensor público asignado", example = "Dr. Juan Pérez")
        private String nombreDefensorPublico;

        @Schema(description = "Nombre del usuario visitado en el caso", example = "Luis Gómez")
        private String nombreUsuarioVisitado;

        @Schema(description = "ID del departamento donde se desarrolló el caso", example = "1")
        private Integer idDepartamento;

        @Schema(description = "ID del municipio donde se desarrolló el caso", example = "101")
        private Integer idMunicipio;

        @Schema(description = "Indica si el caso está activo", example = "true")
        private Boolean activo;

    // Constructor por defecto (necesario para algunas frameworks)
    public CasoDTO() {
    }

    // Constructor con todos los campos
    public CasoDTO(Integer idCaso, String codigoCaso, Integer idDelito,String nombreDefensorPublico,
                   String nombreUsuarioVisitado, Integer idDepartamento, Integer idMunicipio, Boolean activo) {
        this.idCaso = idCaso;
        this.codigoCaso = codigoCaso;
        //this.delito = delito;
        this.idDelito= idDelito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.idDepartamento = idDepartamento; // <-- Aquí se asigna el ID del departamento
        this.idMunicipio = idMunicipio;       // <-- Aquí se asigna el ID del municipio
        this.activo = activo;
    }

    public Integer getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(Integer idCaso) {
        this.idCaso = idCaso;
    }

    public String getCodigoCaso() {
        return codigoCaso;
    }

    public void setCodigoCaso(String codigoCaso) {
        this.codigoCaso = codigoCaso;
    }


    public String getNombreDefensorPublico() {
        return nombreDefensorPublico;
    }

    public void setNombreDefensorPublico(String nombreDefensorPublico) {
        this.nombreDefensorPublico = nombreDefensorPublico;
    }

    public String getNombreUsuarioVisitado() {
        return nombreUsuarioVisitado;
    }

    public void setNombreUsuarioVisitado(String nombreUsuarioVisitado) {
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setIdDelito(Integer idDelito) {
        this.idDelito = idDelito;
    }

    public Integer getIdDelito() {
        return idDelito;
    }
}