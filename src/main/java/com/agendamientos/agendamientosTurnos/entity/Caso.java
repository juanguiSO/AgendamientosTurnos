package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caso")
public class Caso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caso")
    private Integer idCaso;

    @Column(name = "codigo_caso", length = 255)
    private String codigoCaso;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_delito") // Referencia a la columna de clave foránea
    private Delito delito;

    @Column(name = "nombre_defensor_publico", length = 255)
    private String nombreDefensorPublico;

    @Column(name = "nombre_usuario_visitado", length = 255)
    private String nombreUsuarioVisitado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamentos")
    private Departamento departamento;

    // **Nueva relación con la entidad Municipio**
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipio")
    private Municipio municipio;

    @Column(name = "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo;

    @OneToMany(mappedBy = "caso")
    private List<Mision> misiones;

    @ManyToOne
    @JoinColumn(name = "id_viaje")
    private Viaje viaje;

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setIdCaso(Integer idCaso) {
        this.idCaso = idCaso;
    }

    public void setCodigoCaso(String codigoCaso) {
        this.codigoCaso = codigoCaso;
    }

    public void setDelito(Delito delito) {
        this.delito = delito;
    }

    public void setNombreUsuarioVisitado(String nombreUsuarioVisitado) {
        this.nombreUsuarioVisitado = nombreUsuarioVisitado;
    }

    public void setNombreDefensorPublico(String nombreDefensorPublico) {
        this.nombreDefensorPublico = nombreDefensorPublico;
    }

    public Integer getIdCaso() {
        return idCaso;
    }

    public String getNombreUsuarioVisitado() {
        return nombreUsuarioVisitado;
    }

    public String getCodigoCaso() {
        return codigoCaso;
    }

    public Delito getDelito() {
        return delito;
    }

    public String getNombreDefensorPublico() {
        return nombreDefensorPublico;
    }

    public List<Mision> getMisiones() {
        return misiones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

}