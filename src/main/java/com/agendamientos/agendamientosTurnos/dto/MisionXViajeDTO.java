package com.agendamientos.agendamientosTurnos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MisionXViajeDTO {
    private Long id;
    private ViajeDTO viaje; // Un DTO para el viaje
    private MisionDTO mision; // Un DTO para la misión
    // Puedes añadir otros campos si los tienes en MisionXViaje


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ViajeDTO getViaje() {
        return viaje;
    }

    public void setViaje(ViajeDTO viaje) {
        this.viaje = viaje;
    }

    public MisionDTO getMision() {
        return mision;
    }

    public void setMision(MisionDTO mision) {
        this.mision = mision;
    }
}