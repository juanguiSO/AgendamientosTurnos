package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El grado no puede estar en blanco")
    @Size(max = 45, message = "El grado no puede tener más de 45 caracteres")
    @Column(name = "grado", length = 45, nullable = false)
    private String grado;

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