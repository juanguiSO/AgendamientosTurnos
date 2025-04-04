package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
@Entity
@Table(name= "grado")
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Integer idGrado;

    @Column(name = "grado", length = 45, nullable = false)
    private String grado;

    // Eliminado el campo nombreGrado

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activo = true;

    // Constructores getters y setters...

    public Grado() {
    }

    public Grado(String grado, String nombreGrado) {
        this.grado = grado;

    }

    public Integer getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Integer idGrado) {
        this.idGrado = idGrado;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }


    public boolean isActivo() { // Cambié "isActive" a "isActivo"
        return activo;
    }

    public void setActivo(boolean activo) { // Cambié "setActive" a "setActivo"
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Grado{" +
                "idGrado=" + idGrado +
                ", grado='" + grado + '\'' +
                ", activo=" + activo + // Cambié "isActive" a "activo"
                '}';
    }
}
