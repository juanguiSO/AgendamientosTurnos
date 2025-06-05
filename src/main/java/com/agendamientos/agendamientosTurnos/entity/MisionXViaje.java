package com.agendamientos.agendamientosTurnos.entity;


import jakarta.persistence.*; // Importa todas las anotaciones de JPA
import lombok.NoArgsConstructor; // Lombok: Genera un constructor sin argumentos
import lombok.Data; // Lombok: Genera getters, setters, equals, hashCode y toString

import java.io.Serializable; // Buena práctica para entidades JPA

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "mision_x_viaje") // Mapea la entidad a la tabla 'mision_x_viaje' en la base de datos
@Data // Anotación de Lombok para generar getters, setters, equals, hashCode y toString automáticamente
// Anotación de Lombok para generar el constructor por defecto (necesario para JPA)
public class MisionXViaje implements Serializable {

    private static final long serialVersionUID = 1L; // Recomendado para Serializable

    @Id // Marca el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el ID es auto-generado por la base de datos
    @Column(name = "id") // Mapea el campo 'id' a la columna 'id' en la tabla
    private Long id; // Corresponde a la columna 'id' en tus imágenes

    @ManyToOne(fetch = FetchType.LAZY) // Relación Muchos a Uno con la entidad Viaje. Lazy loading es eficiente.
    @JoinColumn(name = "id_viaje", referencedColumnName = "id_viaje", nullable = false) // Mapea a la columna 'id_viaje' que es FK a la PK 'id_viaje' de la tabla 'Viaje'
    private Viaje viaje; // Objeto Viaje al que está asociada esta relación

    @ManyToOne(fetch = FetchType.LAZY) // Relación Muchos a Uno con la entidad Mision. Lazy loading es eficiente.
    @JoinColumn(name = "numero_mision", referencedColumnName = "numero_mision", nullable = false) // Mapea a la columna 'numero_mision' que es FK a la PK 'numero_mision' de la tabla 'Mision'
    private Mision mision; // Objeto Mision al que está asociada esta relación


    public MisionXViaje() {
        // Constructor vacío requerido por JPA
    }

    // Constructor con argumentos para facilitar la creación de nuevas relaciones
    // Si usas @Data, este constructor manual es útil si no quieres un @AllArgsConstructor
    // que incluya el 'id' (que es auto-generado).
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
