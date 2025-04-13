package com.agendamientos.agendamientosTurnos.dto;

public class CasoDTO {

    private Integer idCaso;
    private String codigoCaso;
    private String delito;
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private String nombreDepartamento;
    private String nombreMunicipio;
    private Boolean activo;

    // Constructor por defecto (necesario para algunas frameworks)
    public CasoDTO() {
    }

    // Constructor con todos los campos
    public CasoDTO(Integer idCaso, String codigoCaso, String delito, String nombreDefensorPublico,
                   String nombreUsuarioVisitado, String nombreDepartamento, String nombreMunicipio, Boolean activo) {

        this.idCaso =idCaso;
        this.codigoCaso = codigoCaso;
        this.delito = delito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.nombreDepartamento = nombreDepartamento;
        this.nombreMunicipio = nombreMunicipio;
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