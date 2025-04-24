package com.agendamientos.agendamientosTurnos.dto;

public class CasoDetalleDTO {

    private Integer idCaso;
    private String codigoCaso;
    private String delito;
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private Integer idDepartamento; // ID for the department
    private String nombreDepartamento; // Name of the department
    private Integer idMunicipio; // ID for the municipality
    private String nombreMunicipio; // Name of the municipality
    private Boolean activo;

    // Default constructor
    public CasoDetalleDTO() {
    }

    // Constructor with all fields
    public CasoDetalleDTO(Integer idCaso, String codigoCaso, String delito, String nombreDefensorPublico,
                          String nombreUsuarioVisitado, Integer idDepartamento, String nombreDepartamento,
                          Integer idMunicipio, String nombreMunicipio, Boolean activo) {
        this.idCaso = idCaso;
        this.codigoCaso = codigoCaso;
        this.delito = delito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.activo = activo;
    }

    // Getters and setters for all fields
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

    public String getDelito() {
        return delito;
    }

    public void setDelito(String delito) {
        this.delito = delito;
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