package com.agendamientos.agendamientosTurnos.dto;

import com.agendamientos.agendamientosTurnos.entity.Viaje;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO de resultado para la creación de un viaje.
 * Contiene información sobre el viaje creado, misiones no asignadas y un mensaje adicional.
 */
@Schema(description = "DTO de resultado para la creación de un viaje, incluye el viaje creado, las misiones que no pudieron asignarse y un mensaje.")
public class ViajeCreationResultDTO {

    @Schema(description = "Objeto de viaje que fue creado exitosamente.")
    private Viaje viajeCreado;

    @Schema(description = "Lista de IDs de misiones que no pudieron ser asignadas al viaje por superar la capacidad del vehículo.", example = "[101, 102]")
    private List<Integer> misionesNoAsignadasIds;

    @Schema(description = "Mensaje adicional que explica el resultado de la operación.", example = "Se creó el viaje, pero 2 misiones no fueron asignadas por exceso de capacidad.")
    private String mensajeAdicional;

    /**
     * Constructor para ViajeCreationResultDTO.
     * @param viajeCreado El objeto Viaje que fue creado y guardado.
     * @param misionesNoAsignadasIds Lista de IDs de misiones que no pudieron ser asignadas al viaje.
     * @param mensajeAdicional Un mensaje descriptivo sobre el resultado de la operación.
     */
    public ViajeCreationResultDTO(Viaje viajeCreado, List<Integer> misionesNoAsignadasIds, String mensajeAdicional) {
        this.viajeCreado = viajeCreado;
        this.misionesNoAsignadasIds = misionesNoAsignadasIds;
        this.mensajeAdicional = mensajeAdicional;
    }

    // Getters para acceder a las propiedades del DTO

    public Viaje getViajeCreado() {
        return viajeCreado;
    }

    public List<Integer> getMisionesNoAsignadasIds() {
        return misionesNoAsignadasIds;
    }

    public String getMensajeAdicional() {
        return mensajeAdicional;
    }
}
