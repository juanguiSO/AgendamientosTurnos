package com.agendamientos.agendamientosTurnos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "delito")
public class Delito {

    @Id
    @Column(name = "id_delito", nullable = false)
    private int idDelito;

    @Column(name = "tipo_delito", length = 255, nullable = false)
    private String tipoDelito;

    // Constructor sin argumentos
    public Delito() {
    }

    // Constructor con argumentos
    public Delito(int idDelito, String tipoDelito) {
        this.idDelito = idDelito;
        this.tipoDelito = tipoDelito;
    }

    // Getters y setters
    public int getIdDelito() {
        return idDelito;
    }

    public void setIdDelito(int idDelito) {
        this.idDelito = idDelito;
    }

    public String getTipoDelito() {
        return tipoDelito;
    }

    public void setTipoDelito(String tipoDelito) {
        this.tipoDelito = tipoDelito;
    }
}
