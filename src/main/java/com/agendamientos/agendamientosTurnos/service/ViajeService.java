package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.ViajeCreationDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import com.agendamientos.agendamientosTurnos.repository.CasoRepository;
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.ViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    private static final double DISTANCIA_UMBRAL_VIATICO = 60.0;

    private final ViajeRepository viajeRepository;
    private final CasoRepository casoRepository;
    private final EstadoViajeRepository estadoViajeRepository;
    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public ViajeService(
            ViajeRepository viajeRepository,
            CasoRepository casoRepository,
            EstadoViajeRepository estadoViajeRepository,
            VehiculoRepository vehiculoRepository
    ) {
        this.viajeRepository = viajeRepository;
        this.casoRepository = casoRepository;
        this.estadoViajeRepository = estadoViajeRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Obtiene una lista de todos los viajes activos.
     * Utiliza el método findByActivo del repositorio para mayor eficiencia.
     *
     * @return Una lista de objetos Viaje activos.
     */
    public List<Viaje> findAll() {
        return viajeRepository.findAll();
    }
    public List<Viaje> findByActivo() {
        return viajeRepository.findByActivo(true);
    }

    /**
     * Obtiene un viaje por su ID, solo si está activo.
     * Utiliza el método findByIdViajeAndActivo del repositorio para mayor eficiencia.
     *
     * @param id El ID del viaje.
     * @return Un Optional que contiene el Viaje si se encuentra y está activo, o un Optional vacío si no.
     */
    public Optional<Viaje> findById(Integer id) {
        return viajeRepository.findByIdViajeAndActivo(id, true);
    }

    /**
     * Guarda un nuevo viaje o actualiza uno existente.
     * Aplica la lógica para el cálculo de viáticos y valida las relaciones con EstadoViaje y Vehiculo.
     * Este método está pensado para guardar/actualizar un Viaje sin considerar la asignación de Casos
     * en el mismo paso. Para la creación inicial con asignación de casos, usa 'crearViajeYAsignarCasos'.
     *
     * @param viaje El objeto Viaje a guardar.
     * @return El objeto Viaje guardado/actualizado.
     * @throws RuntimeException Si el EstadoViaje o Vehiculo asociados no existen, o si falta alguno.
     */
    @Transactional
    public Viaje save(Viaje viaje) {
        // Validar y asignar la relación con EstadoViaje
        if (viaje.getEstadoViaje() != null && viaje.getEstadoViaje().getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viaje.getEstadoViaje().getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viaje.getEstadoViaje().getIdEstadoViaje() + " no existe.");
            }
            viaje.setEstadoViaje(existingEstadoViaje.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // Validar y asignar la relación con Vehiculo
        if (viaje.getVehiculo() != null && viaje.getVehiculo().getIdVehiculo() != null) {
            Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viaje.getVehiculo().getIdVehiculo());
            if (existingVehiculo.isEmpty()) {
                throw new RuntimeException("El Vehiculo con ID " + viaje.getVehiculo().getIdVehiculo() + " no existe.");
            }
            viaje.setVehiculo(existingVehiculo.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
        }

        viaje.setViatico(calcularViatico(viaje.getDistanciaRecorrida()));
        viaje.setActivo(true); // Asegurarse de que un nuevo viaje siempre se cree como activo

        return viajeRepository.save(viaje);
    }

    /**
     * Realiza un borrado lógico de un viaje por su ID.
     * Establece el campo 'activo' a false en lugar de eliminar el registro físicamente.
     *
     * @param id El ID del viaje a "eliminar" lógicamente.
     * @throws RuntimeException Si el viaje no es encontrado o ya está inactivo.
     */
    @Transactional
    public void deleteById(Integer id) {
        // Usa findById normal para encontrar el viaje, incluso si está inactivo,
        // para poder lanzar una excepción más específica si ya lo está.
        Optional<Viaje> optionalViaje = viajeRepository.findById(id);
        if (optionalViaje.isPresent()) {
            Viaje viaje = optionalViaje.get();
            if (!viaje.isActivo()) {
                throw new RuntimeException("El viaje con ID " + id + " ya está inactivo.");
            }
            viaje.setActivo(false); // Marcar como inactivo
            viajeRepository.save(viaje); // Guardar el cambio
            System.out.println("Viaje ID " + id + " marcado como inactivo (borrado lógico).");
        } else {
            throw new RuntimeException("Viaje no encontrado con ID: " + id);
        }
    }


    /**
     * Actualiza un viaje existente.
     * Aplica la lógica para el cálculo de viáticos y valida las relaciones con EstadoViaje y Vehiculo.
     * Solo permite la actualización si el viaje está activo.
     *
     * @param id El ID del viaje a actualizar.
     * @param viajeDetails El objeto Viaje con los detalles actualizados.
     * @return El objeto Viaje actualizado.
     * @throws RuntimeException Si el viaje no es encontrado, ya está inactivo, o si las entidades relacionadas no existen o faltan.
     */
    @Transactional
    public Viaje update(Integer id, Viaje viajeDetails) {
        // Usa findById normal para encontrar el viaje, incluso si está inactivo,
        // para poder lanzar una excepción más específica si ya lo está.
        Optional<Viaje> optionalViaje = viajeRepository.findById(id);
        if (optionalViaje.isPresent()) {
            Viaje viaje = optionalViaje.get();

            viaje.setActivo(viajeDetails.isActivo());

            viaje.setTiempoFin(viajeDetails.getTiempoFin());
            viaje.setTiempoInicio(viajeDetails.getTiempoInicio());
            viaje.setDistanciaRecorrida(viajeDetails.getDistanciaRecorrida());
            // El campo 'estado' fue removido de la entidad Viaje en una interacción anterior,
            // por lo que la línea de asignación de 'estado' se elimina aquí.
            // viaje.setEstado(viajeDetails.getEstado());

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

            // Actualizar la relación con Vehiculo
            if (viajeDetails.getVehiculo() != null && viajeDetails.getVehiculo().getIdVehiculo() != null) {
                Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDetails.getVehiculo().getIdVehiculo());
                if (existingVehiculo.isEmpty()) {
                    throw new RuntimeException("El Vehiculo con ID " + viajeDetails.getVehiculo().getIdVehiculo() + " no existe para la actualización.");
                }
                viaje.setVehiculo(existingVehiculo.get());
            } else {
                throw new RuntimeException("Se debe proporcionar un Vehiculo para actualizar el viaje.");
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
        return distanciaRecorrida != null && distanciaRecorrida > DISTANCIA_UMBRAL_VIATICO;
    }

    /**
     * Crea un nuevo viaje y lo asigna a una lista de casos existentes.
     *
     * @param viajeDTO El DTO que contiene los detalles del viaje y los IDs de los casos a asignar.
     * @return El objeto Viaje guardado con sus casos asignados.
     * @throws RuntimeException Si el EstadoViaje o Vehiculo asociados no existen, o si un Caso no es encontrado.
     */
    @Transactional
    public Viaje crearViajeYAsignarCasos(ViajeCreationDTO viajeDTO) {
        Viaje nuevoViaje = new Viaje();
        nuevoViaje.setTiempoInicio(viajeDTO.getTiempoInicio());
        nuevoViaje.setTiempoFin(viajeDTO.getTiempoFin());
        nuevoViaje.setDistanciaRecorrida(viajeDTO.getDistanciaRecorrida());
        // El campo 'estado' fue removido de la entidad Viaje en una interacción anterior,
        // por lo que la línea de asignación de 'estado' se elimina aquí.
        // nuevoViaje.setEstado(viajeDTO.getEstado());

        // Validar y asignar la relación con EstadoViaje
        if (viajeDTO.getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viajeDTO.getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viajeDTO.getIdEstadoViaje() + " no existe.");
            }
            nuevoViaje.setEstadoViaje(existingEstadoViaje.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // Validar y asignar la relación con Vehiculo
        if (viajeDTO.getIdVehiculo() != null) {
            Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDTO.getIdVehiculo());
            if (existingVehiculo.isEmpty()) {
                throw new RuntimeException("El Vehiculo con ID " + viajeDTO.getIdVehiculo() + " no existe.");
            }
            nuevoViaje.setVehiculo(existingVehiculo.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
        }

        nuevoViaje.setViatico(calcularViatico(nuevoViaje.getDistanciaRecorrida()));
        nuevoViaje.setActivo(true); // Asegurarse de que un nuevo viaje siempre se cree como activo

        Viaje viajeGuardado = viajeRepository.save(nuevoViaje);

        if (viajeDTO.getIdCasos() != null && !viajeDTO.getIdCasos().isEmpty()) {
            for (Integer idCaso : viajeDTO.getIdCasos()) {
                Optional<Caso> casoOptional = casoRepository.findById(idCaso);
                if (casoOptional.isPresent()) {
                    Caso caso = casoOptional.get();
                    caso.setViaje(viajeGuardado);
                    casoRepository.save(caso);
                    // Asegurarse de que la lista 'casos' esté inicializada antes de añadir
                    if (viajeGuardado.getCasos() == null) {
                        viajeGuardado.setCasos(new ArrayList<>());
                    }
                    viajeGuardado.getCasos().add(caso); // Añadir directamente a la lista
                } else {
                    System.err.println("Advertencia: Caso con ID " + idCaso + " no encontrado para asignación al viaje.");
                }
            }
        }

        return viajeGuardado;
    }

    /**
     * Asigna o reasigna una lista de casos a un viaje existente.
     * Los casos que ya no estén en la lista proporcionada serán desvinculados del viaje.
     * Solo permite la asignación si el viaje está activo.
     *
     * @param idViaje El ID del viaje al que se asignarán los casos.
     * @param idCasosAAsignar La lista de IDs de casos que deben estar asociados a este viaje.
     * @return El objeto Viaje actualizado.
     * @throws RuntimeException Si el viaje o alguno de los casos no es encontrado, o si el viaje está inactivo.
     */
    @Transactional
    public Viaje asignarCasosExistentesAViaje(Integer idViaje, List<Integer> idCasosAAsignar) {
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con ID: " + idViaje));

        // Solo permitir la asignación si el viaje está activo
        if (!viaje.isActivo()) {
            throw new RuntimeException("No se pueden asignar casos a un viaje inactivo con ID: " + idViaje);
        }

        List<Caso> casosActualesDelViaje = casoRepository.findByViaje(viaje);

        for (Caso caso : casosActualesDelViaje) {
            if (!idCasosAAsignar.contains(caso.getIdCaso())) {
                caso.setViaje(null);
                casoRepository.save(caso);
            }
        }

        if (idCasosAAsignar != null && !idCasosAAsignar.isEmpty()) {
            for (Integer idCaso : idCasosAAsignar) {
                Caso caso = casoRepository.findById(idCaso)
                        .orElseThrow(() -> new RuntimeException("Caso no encontrado con ID: " + idCaso));
                if (caso.getViaje() == null || !caso.getViaje().equals(viaje)) {
                    caso.setViaje(viaje);
                    casoRepository.save(caso);
                }
            }
        }
        return viaje;
    }
}
