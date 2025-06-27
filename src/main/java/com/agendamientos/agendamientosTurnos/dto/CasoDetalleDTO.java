package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;


    @Schema(description = "Detalle de un caso, incluyendo información del delito, ubicación y actores involucrados.")
    public class CasoDetalleDTO {

        @Schema(description = "Identificador único del caso", example = "1")
        private Integer idCaso;

        @Schema(description = "Código único del caso, numero de 21 digitos", example = "012301230123202100520")
        private String codigoCaso;

        @Schema(description = "Identificador del delito asociado", example = "5")
        private Integer idDelito;

        @Schema(description = "Nombre o tipo del delito", example = "Hurto calificado")
        private String tipoDelito;

        @Schema(description = "Nombre completo del defensor público asignado", example = "Dra. Carolina Gómez")
        private String nombreDefensorPublico;

        @Schema(description = "Nombre del usuario visitado en el caso", example = "Juan Pérez")
        private String nombreUsuarioVisitado;

        @Schema(description = "ID del departamento donde ocurrió el caso", example = "1")
        private Integer idDepartamento;

        @Schema(description = "Nombre del departamento", example = "Antioquia")
        private String nombreDepartamento;

        @Schema(description = "ID del municipio donde ocurrió el caso", example = "101")
        private Integer idMunicipio;

        @Schema(description = "Nombre del municipio", example = "Medellín")
        private String nombreMunicipio;

        @Schema(description = "Indica si el caso está activo", example = "true")
        private Boolean activo;


    // Constructor por defecto
    public CasoDetalleDTO() {
    }

    // Constructor con todos los campos
    public CasoDetalleDTO(Integer idCaso, String codigoCaso, Integer idDelito, String tipoDelito,
                          String nombreDefensorPublico, String nombreUsuarioVisitado, Integer idDepartamento,
                          String nombreDepartamento, Integer idMunicipio, String nombreMunicipio, Boolean activo) {
        this.idCaso = idCaso;
        this.codigoCaso = codigoCaso;
        this.idDelito = idDelito;
        this.tipoDelito = tipoDelito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.activo = activo;
    }

    // Getters y setters para todos los campos

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

    public Integer getIdDelito() {
        return idDelito;
    }

    public void setIdDelito(Integer idDelito) {
        this.idDelito = idDelito;
    }

    public String getTipoDelito() {
        return tipoDelito;
    }

    public void setTipoDelito(String tipoDelito) {
        this.tipoDelito = tipoDelito;
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

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}