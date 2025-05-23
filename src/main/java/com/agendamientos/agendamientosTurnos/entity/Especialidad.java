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
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private int idEspecialidad;

    @NotBlank(message = "La especialidad no puede estar en blanco")
    @Size(max = 45, message = "La especialidad no puede tener más de 45 caracteres")
    @Column(name = "especialidad", length = 45, nullable = false)
    private String especialidad;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activo = true; // Cambié "isActive" a "activo"

    // Constructores (por defecto y parametrizado), getters y setters...

    public Especialidad() {
    }

    public Especialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public boolean isActivo() { // Cambié "isActive" a "isActivo"
        return activo;
    }

    public void setActivo(boolean activo) { // Cambié "setActive" a "setActivo"
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Especialidad{" +
                "idEspecialidad=" + idEspecialidad +
                ", especialidad='" + especialidad + '\'' +
                ", activo=" + activo + // Cambié "isActive" a "activo"
                '}';
    }
}