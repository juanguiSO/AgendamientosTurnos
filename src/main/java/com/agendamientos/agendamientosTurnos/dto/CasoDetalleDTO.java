package com.agendamientos.agendamientosTurnos.dto;

public class CasoDetalleDTO {

    private Integer idCaso;
    private String codigoCaso;
    private Integer idDelito;         // ID del delito
    private String tipoDelito;       // Tipo del delito
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private Integer idDepartamento;
    private String nombreDepartamento;
    private Integer idMunicipio;
    private String nombreMunicipio;
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