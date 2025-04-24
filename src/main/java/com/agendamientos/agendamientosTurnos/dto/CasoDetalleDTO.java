package com.agendamientos.agendamientosTurnos.dto;

public class CasoDetalleDTO {

    private Integer idCaso;
    private String codigoCaso;
    private String delito;
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private String nombreDepartamento; // Campo para el nombre del departamento
    private String nombreMunicipio;    // Campo para el nombre del municipio
    private Boolean activo;

    // Constructor por defecto
    public CasoDetalleDTO() {
    }

    // Constructor con todos los campos
    public CasoDetalleDTO(Integer idCaso, String codigoCaso, String delito, String nombreDefensorPublico,
                          String nombreUsuarioVisitado, String nombreDepartamento, String nombreMunicipio, Boolean activo) {
        this.idCaso = idCaso;
        this.codigoCaso = codigoCaso;
        this.delito = delito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.nombreDepartamento = nombreDepartamento;
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

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
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