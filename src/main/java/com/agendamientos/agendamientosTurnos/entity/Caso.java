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

    @Column(name = "delito", length = 255)
    private String delito;

    @Column(name = "nombre_defensor_publico", length = 255)
    private String nombreDefensorPublico;

    @Column(name = "nombre_usuario_visitado", length = 255)
    private String nombreUsuarioVisitado;

    @Column(name = "id_departamentos")
    private Integer idDepartamentos;

    @Column(name = "id_municipio")
    private Integer idMunicipio;

    @Column(name = "activo", columnDefinition = "TINYINT(1)")
    private Boolean activo; // Usamos Boolean para permitir valores nulos si la base de datos lo permite

    @OneToMany(mappedBy = "caso") // Asumiendo una relación con otra entidad llamada Mision
    private List<Mision> misiones;

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getIdDepartamentos() {
        return idDepartamentos;
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

    public String getDelito() {
        return delito;
    }

    public String getNombreDefensorPublico() {
        return nombreDefensorPublico;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public List<Mision> getMisiones() {
        return misiones;
    }

    public Boolean getActivo() {
        return activo;
    }
}