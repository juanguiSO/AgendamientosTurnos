package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.entity.Vehiculo; // Asumo que ya tienes esta entidad
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.ViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.VehiculoRepository; // Asumo que ya tienes este repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class ViajeService {

    // Constante para el umbral de distancia para viáticos
    private static final double DISTANCIA_UMBRAL_VIATICO = 60.0;

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private EstadoViajeRepository estadoViajeRepository;

    // Se mantiene 'required = false' si VehiculoRepository no está 100% listo,
    // pero en un entorno de producción deberías asegurar su existencia y quitarlo.
    @Autowired(required = false)
    private VehiculoRepository vehiculoRepository;

    /**
     * Obtiene una lista de todos los viajes.
     *
     * @return Una lista de objetos Viaje.
     */
    public List<Viaje> findAll() {
        return viajeRepository.findAll();
    }

    /**
     * Obtiene un viaje por su ID.
     *
     * @param id El ID del viaje.
     * @return Un Optional que contiene el Viaje si se encuentra, o un Optional vacío si no.
     */
    public Optional<Viaje> findById(Integer id) {
        return viajeRepository.findById(id);
    }

    /**
     * Guarda un nuevo viaje o actualiza uno existente.
     * Aplica la lógica para el cálculo de viáticos y valida las relaciones con EstadoViaje y Vehiculo.
     *
     * @param viaje El objeto Viaje a guardar.
     * @return El objeto Viaje guardado/actualizado.
     * @throws RuntimeException Si el EstadoViaje o Vehiculo asociados no existen, o si falta alguno.
     */
    public Viaje save(Viaje viaje) {
        // Validar y asignar la relación con EstadoViaje
        if (viaje.getEstadoViaje() != null && viaje.getEstadoViaje().getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viaje.getEstadoViaje().getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viaje.getEstadoViaje().getIdEstadoViaje() + " no existe.");
            }
            viaje.setEstadoViaje(existingEstadoViaje.get()); // Asigna el objeto gestionado por JPA
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // Validar y asignar la relación con Vehiculo (solo si el repositorio está disponible)
        if (vehiculoRepository != null) {
            if (viaje.getVehiculo() != null && viaje.getVehiculo().getIdVehiculo() != null) {
                Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viaje.getVehiculo().getIdVehiculo());
                if (existingVehiculo.isEmpty()) {
                    throw new RuntimeException("El Vehiculo con ID " + viaje.getVehiculo().getIdVehiculo() + " no existe.");
                }
                viaje.setVehiculo(existingVehiculo.get()); // Asigna el objeto gestionado por JPA
            } else {
                throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
            }
        }

        // Lógica para calcular el viático basado en la distancia recorrida
        viaje.setViatico(calcularViatico(viaje.getDistanciaRecorrida()));

        return viajeRepository.save(viaje);
    }

    /**
     * Elimina un viaje por su ID.
     *
     * @param id El ID del viaje a eliminar.
     * @throws RuntimeException Si el viaje no es encontrado.
     */
    public void deleteById(Integer id) {
        if (!viajeRepository.existsById(id)) {
            throw new RuntimeException("Viaje no encontrado con ID: " + id);
        }
        viajeRepository.deleteById(id);
    }

    /**
     * Actualiza un viaje existente.
     * Aplica la lógica para el cálculo de viáticos y valida las relaciones con EstadoViaje y Vehiculo.
     *
     * @param id El ID del viaje a actualizar.
     * @param viajeDetails El objeto Viaje con los detalles actualizados.
     * @return El objeto Viaje actualizado.
     * @throws RuntimeException Si el viaje no es encontrado, o si las entidades relacionadas no existen o faltan.
     */
    public Viaje update(Integer id, Viaje viajeDetails) {
        Optional<Viaje> optionalViaje = viajeRepository.findById(id);
        if (optionalViaje.isPresent()) {
            Viaje viaje = optionalViaje.get();

            // Actualiza los campos simples
            viaje.setTiempoFin(viajeDetails.getTiempoFin());
            viaje.setTiempoInicio(viajeDetails.getTiempoInicio());
            viaje.setDistanciaRecorrida(viajeDetails.getDistanciaRecorrida()); // Actualiza la distancia

            // Recalcular el viático basado en la nueva distancia recorrida
            viaje.setViatico(calcularViatico(viaje.getDistanciaRecorrida()));

            // Actualizar la relación con EstadoViaje
            if (viajeDetails.getEstadoViaje() != null && viajeDetails.getEstadoViaje().getIdEstadoViaje() != null) {
                Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viajeDetails.getEstadoViaje().getIdEstadoViaje());
                if (existingEstadoViaje.isEmpty()) {
                    throw new RuntimeException("El EstadoViaje con ID " + viajeDetails.getEstadoViaje().getIdEstadoViaje() + " no existe para la actualización.");
                }
                viaje.setEstadoViaje(existingEstadoViaje.get());
            } else {
                throw new RuntimeException("Se debe proporcionar un EstadoViaje para actualizar el viaje.");
            }

            // Actualizar la relación con Vehiculo (solo si el repositorio está disponible)
            if (vehiculoRepository != null) {
                if (viajeDetails.getVehiculo() != null && viajeDetails.getVehiculo().getIdVehiculo() != null) {
                    Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDetails.getVehiculo().getIdVehiculo());
                    if (existingVehiculo.isEmpty()) {
                        throw new RuntimeException("El Vehiculo con ID " + viajeDetails.getVehiculo().getIdVehiculo() + " no existe para la actualización.");
                    }
                    viaje.setVehiculo(existingVehiculo.get());
                } else {
                    throw new RuntimeException("Se debe proporcionar un Vehiculo para actualizar el viaje.");
                }
            }

            return viajeRepository.save(viaje);
        } else {
            throw new RuntimeException("Viaje no encontrado con ID: " + id);
        }
    }

    /**
     * Método auxiliar para calcular si un viaje lleva viático.
     *
     * @param distanciaRecorrida La distancia recorrida del viaje.
     * @return true si la distancia recorrida es mayor que el umbral de viático, false en caso contrario.
     */
    private boolean calcularViatico(Double distanciaRecorrida) {
        // Si la distancia es nula, se asume que no hay viático.
        return distanciaRecorrida != null && distanciaRecorrida > DISTANCIA_UMBRAL_VIATICO;
    }
}