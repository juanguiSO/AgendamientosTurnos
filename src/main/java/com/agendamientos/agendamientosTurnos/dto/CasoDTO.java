package com.agendamientos.agendamientosTurnos.dto;

public class CasoDTO {

    private Integer idCaso;
    private String codigoCaso;
    //private String delito;
    private Integer idDelito;
    private String nombreDefensorPublico;
    private String nombreUsuarioVisitado;
    private Integer idDepartamento; // <-- Cambiado a Integer
    private Integer idMunicipio;    // <-- Cambiado a Integer
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