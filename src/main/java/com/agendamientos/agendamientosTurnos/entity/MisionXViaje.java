package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor; // Necesario para el constructor sin argumentos

@Entity
@Table(name = "mision_x_viaje")
// Se añade @NoArgsConstructor de Lombok si no lo tenías para el constructor por defecto
@NoArgsConstructor // Necesario para que JPA pueda instanciar la entidad
public class MisionXViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Se recomienda Lazy loading para evitar cargar todo al inicio
    @JoinColumn(name = "numero_mision", nullable = false, referencedColumnName = "numero_mision")
    private Mision mision;

    @ManyToOne(fetch = FetchType.LAZY) // Se recomienda Lazy loading
    @JoinColumn(name = "id_viaje", nullable = false)
    private Viaje viaje;

    // ¡ESTE ES EL CONSTRUCTOR CORREGIDO!
    // Asigna los parámetros a los campos de la clase.
    public MisionXViaje(Viaje viaje, Mision mision) {
        this.viaje = viaje;
        this.mision = mision;
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mision getMision() {
        return mision;
    }

    public void setMision(Mision mision) {
        this.mision = mision;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
}
