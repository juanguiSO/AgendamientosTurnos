package com.agendamientos.agendamientosTurnos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa la relación entre una misión y un viaje.")
public class MisionXViajeDTO {

    @Schema(description = "ID único de la relación entre la misión y el viaje.", example = "101")
    private Long id;

    @Schema(description = "Información detallada del viaje asociado a la misión.")
    private ViajeDTO viaje; // Asegúrate de que ViajeDTO también tenga anotaciones Swagger

    @Schema(description = "Información detallada de la misión asociada al viaje.")
    private MisionDTO mision; // Asegúrate de que MisionDTO tenga anotaciones Swagger

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