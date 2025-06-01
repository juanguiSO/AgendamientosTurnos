package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interfaz es un componente de Spring para acceso a datos
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    /**
     * Busca y devuelve una lista de viajes por su estado.
     * Utilizado por el scheduler para encontrar viajes programados.
     *
     * @param estadoViaje El objeto EstadoViaje por el cual se buscarán los viajes.
     * @return Una lista de objetos Viaje con el estado dado.
     */
    List<Viaje> findByEstadoViaje(EstadoViaje estadoViaje);

    /**
     * Busca y devuelve una lista de todos los viajes que están activos.
     * Esto reemplaza la necesidad de filtrar en el servicio con stream().filter(Viaje::isActivo).
     *
     * @param activo Un valor booleano para filtrar por el estado de actividad (true para activos).
     * @return Una lista de objetos Viaje que están activos.
     */
    List<Viaje> findByActivo(boolean activo);

    /**
     * Busca y devuelve un viaje por su ID, solo si está activo.
     * Se cambió 'findByIdAndActivo' a 'findByIdViajeAndActivo' para coincidir con el nombre de la propiedad 'idViaje' en la entidad Viaje.
     *
     * @param idViaje El ID del viaje.
     * @param activo Un valor booleano para filtrar por el estado de actividad (true para activos).
     * @return Un Optional que contiene el Viaje si se encuentra y está activo, o un Optional vacío si no.
     */
    Optional<Viaje> findByIdViajeAndActivo(Integer idViaje, boolean activo);


    /**
     * Busca viajes para un vehículo específico que se solapen con un rango de tiempo dado.
     * Esto es útil para verificar la disponibilidad del vehículo.
     *
     * @param vehiculo El objeto Vehiculo para el cual se buscan los viajes.
     * @param tiempoInicioNuevoViaje La fecha y hora de inicio del nuevo viaje.
     * @param tiempoFinNuevoViaje La fecha y hora de fin del nuevo viaje.
     * @return Una lista de Viajes existentes que se solapan con el rango de tiempo.
     */
    List<Viaje> findByVehiculoAndTiempoFinAfterAndTiempoInicioBefore(
            Vehiculo vehiculo,
            LocalDateTime tiempoInicioNuevoViaje,
            LocalDateTime tiempoFinNuevoViaje
    );


}
