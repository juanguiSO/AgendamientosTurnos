package com.agendamientos.agendamientosTurnos.dto;


import com.agendamientos.agendamientosTurnos.entity.Viaje;

import java.util.List;

/**
 * DTO de resultado para el método de creación de viajes.
 * Contiene el viaje creado, los IDs de las misiones que no pudieron ser asignadas
 * y un mensaje adicional informativo.
 */
public class ViajeCreationResultDTO {
    private Viaje viajeCreado;
    private List<Integer> misionesNoAsignadasIds; // IDs de las misiones que excedieron la capacidad del vehículo
    private String mensajeAdicional; // Mensaje informativo para el usuario

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
