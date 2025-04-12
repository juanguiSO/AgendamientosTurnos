package com.agendamientos.agendamientosTurnos.dto;

public class CasoDTO {
    private String codigoCaso;
    private String delito;
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private String departamentos;
    private String municipios;
    private Boolean activo;

    public CasoDTO(String codigoCaso, String delito, String nombreDefensorPublico, String nombreUsuarioVisitado, String departamentos, String municipios, Boolean activo) {
        this.codigoCaso = codigoCaso;
        this.delito = delito;
        this.nombreDefensorPublico = nombreDefensorPublico;
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
        this.departamentos = departamentos;
        this.municipios = municipios;
        this.activo = activo;
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

    public String getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(String departamentos) {
        this.departamentos = departamentos;
    }

    public String getMunicipios() {
        return municipios;
    }

    public void setMunicipios(String municipios) {
        this.municipios = municipios;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
