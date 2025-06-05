package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.MisionDTO;
import com.agendamientos.agendamientosTurnos.dto.ViajeCreationDTO;
import com.agendamientos.agendamientosTurnos.dto.ViajeCreationResultDTO;
import com.agendamientos.agendamientosTurnos.dto.ViajeCreationWithMisionesDTO;
import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

@Service
public class ViajeService {

    private static final Logger logger = LoggerFactory.getLogger(ViajeService.class); // Instancia del logger

    // Constante para el umbral de distancia para el cálculo del viático
    private static final double DISTANCIA_UMBRAL_VIATICO = 60.0;

    // Inyección de dependencias de los repositorios
    private final ViajeRepository viajeRepository;
    private final CasoRepository casoRepository;
    private final EstadoViajeRepository estadoViajeRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MisionXViajeRepository misionXViajeRepository;
    private final MisionRepository misionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public ViajeService(
            ViajeRepository viajeRepository,
            CasoRepository casoRepository,
            EstadoViajeRepository estadoViajeRepository,
            VehiculoRepository vehiculoRepository,
            MisionXViajeRepository misionXViajeRepository,
            MisionRepository misionRepository,
            FuncionarioRepository funcionarioRepository,
            EspecialidadRepository especialidadRepository
    ) {
        this.viajeRepository = viajeRepository;
        this.casoRepository = casoRepository;
        this.estadoViajeRepository = estadoViajeRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.misionXViajeRepository = misionXViajeRepository;
        this.misionRepository = misionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.especialidadRepository = especialidadRepository;
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
     * Método auxiliar para calcular el viático.
     * Si la distancia recorrida es mayor que el umbral, se asigna true, de lo contrario false.
     *
     * @param distanciaRecorrida La distancia recorrida del viaje.
     * @return true si aplica viático, false en caso contrario.
     */
    private boolean calcularViatico(Double distanciaRecorrida) {
        if (distanciaRecorrida == null) {
            return false; // O el valor por defecto que consideres apropiado si la distancia es nula
        }
        return distanciaRecorrida > DISTANCIA_UMBRAL_VIATICO;
    }

    /**
     * Crea un nuevo viaje, valida la capacidad del vehículo basada en las misiones asignadas
     * y asigna los casos al viaje. Las misiones que exceden la capacidad no se asignan
     * y se reportan en el DTO de resultado.
     *
     * @param viajeDTO DTO con los datos para crear el viaje y los IDs de los casos a asignar.
     * @return ViajeCreationResultDTO conteniendo el viaje creado y las misiones no asignadas.
     * @throws RuntimeException si alguna validación crítica falla (ej. entidad no encontrada, vehículo no disponible).
     */
    @Transactional // Asegura que todas las operaciones de base de datos se manejen como una sola transacción
    public ViajeCreationResultDTO crearViajeYAsignarCasos(ViajeCreationDTO viajeDTO) {
        // 1. Inicialización del nuevo Viaje
        Viaje nuevoViaje = new Viaje();
        nuevoViaje.setTiempoInicio(viajeDTO.getTiempoInicio());
        nuevoViaje.setTiempoFin(viajeDTO.getTiempoFin());
        nuevoViaje.setDistanciaRecorrida(viajeDTO.getDistanciaRecorrida());

        // 2. Validación y asignación de EstadoViaje
        if (viajeDTO.getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viajeDTO.getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viajeDTO.getIdEstadoViaje() + " no existe.");
            }
            nuevoViaje.setEstadoViaje(existingEstadoViaje.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // 3. Validación y asignación de Vehiculo
        Vehiculo vehiculoSeleccionado = null;
        if (viajeDTO.getIdVehiculo() != null) {
            Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDTO.getIdVehiculo());
            if (existingVehiculo.isEmpty()) {
                throw new RuntimeException("El Vehiculo con ID " + viajeDTO.getIdVehiculo() + " no existe.");
            }
            vehiculoSeleccionado = existingVehiculo.get();
            nuevoViaje.setVehiculo(vehiculoSeleccionado);
        } else {
            throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
        }

        // --- CORRECCIÓN: Declarar placaVehiculo aquí para que sea accesible en todo el método ---
        String placaVehiculo = vehiculoSeleccionado.getPlaca();

        // --- VALIDACIÓN 1: Un vehículo no puede hacer dos viajes en la misma fecha ---
        // Se busca si ya existe un viaje para este vehículo que se solape con las fechas del nuevo viaje.
        List<Viaje> viajesExistentesParaVehiculo = viajeRepository.findByVehiculoAndTiempoFinAfterAndTiempoInicioBefore(
                vehiculoSeleccionado, nuevoViaje.getTiempoInicio(), nuevoViaje.getTiempoFin()
        );

        if (!viajesExistentesParaVehiculo.isEmpty()) {
            // Si se encuentra algún viaje que se solapa, se lanza una excepción.
            throw new RuntimeException(
                    "El vehículo con placa " + placaVehiculo + " ya está programado para otro viaje en el rango de fechas proporcionado."
            );
        }
        // --- FIN DE VALIDACIÓN 1 ---

        // 4. Determinación de la capacidad máxima del vehículo
        Pattern patronCarro = Pattern.compile("^[A-Z]{3}[0-9]{3}$"); // Formato AAA123
        Pattern patronMoto = Pattern.compile("^[A-Z]{3}[0-9]{2}[A-Z]$"); // Formato AAA23A

        Matcher matcherCarro = patronCarro.matcher(placaVehiculo);
        Matcher matcherMoto = patronMoto.matcher(placaVehiculo);

        int capacidadMaximaPasajeros; // Representa personas ADICIONALES al conductor
        String tipoVehiculo;

        if (matcherCarro.matches()) {
            capacidadMaximaPasajeros = 4; // Un carro puede llevar 4 pasajeros adicionales (total 5 con conductor)
            tipoVehiculo = "carro";
        } else if (matcherMoto.matches()) {
            capacidadMaximaPasajeros = 2; // Una moto puede llevar 1 pasajero adicional (total 2 con conductor)
            tipoVehiculo = "moto";
        } else {
            throw new RuntimeException("El formato de la placa '" + placaVehiculo + "' del vehículo no es reconocido (no es carro ni moto).");
        }

        // 5. Procesar Casos y Misiones para determinar la asignación según la capacidad
        Set<Integer> misionesAsignadasIds = new HashSet<>(); // IDs de misiones que SÍ se asignarán a este viaje
        List<Integer> misionesNoAsignadasIds = new ArrayList<>(); // IDs de misiones que NO se pudieron asignar
        List<Caso> casosParaAsignar = new ArrayList<>(); // Objetos Caso validados que se procesarán

        if (viajeDTO.getIdCasos() != null && !viajeDTO.getIdCasos().isEmpty()) {
            for (Integer idCaso : viajeDTO.getIdCasos()) {
                Optional<Caso> casoOptional = casoRepository.findById(idCaso);
                if (casoOptional.isPresent()) {
                    Caso caso = casoOptional.get();
                    // ASUMIMOS: La entidad Caso tiene una relación @OneToMany con Mision
                    // y que caso.getMisiones() carga las misiones asociadas a ese caso.
                    // Si las misiones se cargan de forma Lazy, considera usar un JOIN FETCH en tu repositorio
                    // o acceder a ellas dentro de esta transacción para evitar LazyInitializationException.
                    if (caso.getMisiones() != null) {
                        for (Mision mision : caso.getMisiones()) {
                            // --- VALIDACIÓN 2: Verificar si la misión ya está programada ---
                            // Si una misión ya tiene un caso asignado, y ese caso ya está en un viaje,
                            // entonces la misión ya está programada.
                            if (mision.getCaso() != null && mision.getCaso().getViaje() != null && mision.getCaso().getViaje().getIdViaje() != null) {
                                // Si la misión ya está programada, la añadimos a las no asignadas
                                misionesNoAsignadasIds.add(mision.getNumeroMision());
                                System.out.println("ADVERTENCIA: La misión con ID " + mision.getNumeroMision() + " ya está programada en el viaje con ID " + mision.getCaso().getViaje().getIdViaje() + ". No se asignará a este nuevo viaje.");
                                continue; // Pasamos a la siguiente misión
                            }
                            // --- FIN DE VALIDACIÓN 2 ---

                            // Si todavía hay capacidad en el vehículo para más personas (misiones)
                            if (misionesAsignadasIds.size() < capacidadMaximaPasajeros) {
                                misionesAsignadasIds.add(mision.getNumeroMision()); // Añadir la misión a las asignadas
                            } else {
                                // No hay más capacidad, esta misión no se puede asignar en este viaje
                                misionesNoAsignadasIds.add(mision.getNumeroMision());
                            }
                        }
                    }
                    casosParaAsignar.add(caso); // Añadimos el caso validado a la lista para usarlo después
                } else {
                    // Si un caso no existe, es un error grave que impide la creación del viaje.
                    throw new RuntimeException("Caso con ID " + idCaso + " no encontrado. No se puede proceder con la creación del viaje.");
                }
            }
        }

        // 6. Preparar mensaje adicional si hay misiones no asignadas
        String mensajeAdicional = null;
        if (!misionesNoAsignadasIds.isEmpty()) {
            // Contar las misiones no asignadas por capacidad vs. ya programadas
            long misionesPorCapacidad = misionesNoAsignadasIds.stream()
                    .filter(id -> !misionesAsignadasIds.contains(id)) // Misiones que no se asignaron por capacidad
                    .count();
            long misionesYaProgramadas = misionesNoAsignadasIds.size() - misionesPorCapacidad;

            StringBuilder sb = new StringBuilder();
            if (misionesPorCapacidad > 0) {
                sb.append(String.format(
                        "El vehículo tipo %s (placa %s) solo puede transportar a %d personas. %d misiones excedieron la capacidad. ",
                        tipoVehiculo, placaVehiculo, capacidadMaximaPasajeros, misionesPorCapacidad
                ));
            }
            if (misionesYaProgramadas > 0) {
                sb.append(String.format(
                        "%d misiones ya estaban programadas en otros viajes. ",
                        misionesYaProgramadas
                ));
            }
            sb.append(String.format("Las misiones con IDs [%s] no fueron asignadas a este viaje. Por favor, gestione estas misiones en otro viaje.",
                    misionesNoAsignadasIds.stream().map(Object::toString).collect(Collectors.joining(", "))
            ));
            mensajeAdicional = sb.toString();
            System.out.println(mensajeAdicional); // También podrías usar un logger (ej. Slf4j)
        }

        // 7. Calcular viático y establecer activo
        nuevoViaje.setViatico(calcularViatico(nuevoViaje.getDistanciaRecorrida()));
        nuevoViaje.setActivo(true); // Asegurarse de que un nuevo viaje siempre se cree como activo

        // 8. Guardar el nuevo Viaje en la base de datos
        Viaje viajeGuardado = viajeRepository.save(nuevoViaje);

        // 9. Asignación REAL de Casos al Viaje (solo los que tienen misiones asignadas)
        if (!casosParaAsignar.isEmpty()) {
            for (Caso caso : casosParaAsignar) {
                // Filtramos las misiones de este caso que SÍ fueron asignadas al viaje actual
                List<Mision> misionesDelCasoAsignadasAlViaje = caso.getMisiones().stream()
                        .filter(mision -> misionesAsignadasIds.contains(mision.getNumeroMision()))
                        .collect(Collectors.toList());

                // Si este caso tiene al menos una misión que se asignó a este viaje, lo asociamos
                if (!misionesDelCasoAsignadasAlViaje.isEmpty()) {
                    caso.setViaje(viajeGuardado); // Asignar el viaje recién guardado al caso
                    casoRepository.save(caso); // Persistir el cambio en el caso

                    // Opcional: Actualizar la colección de casos en el objeto viajeGuardado en memoria
                    if (viajeGuardado.getCasos() == null) {
                        viajeGuardado.setCasos(new ArrayList<>());
                    }
                    viajeGuardado.getCasos().add(caso);
                }
            }
        }

        // 10. Retornar el resultado encapsulado en el DTO
        return new ViajeCreationResultDTO(viajeGuardado, misionesNoAsignadasIds, mensajeAdicional);
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

    @Transactional // Asegura que todas las operaciones de base de datos se manejen como una sola transacción
    public ViajeCreationResultDTO crearViajeYAsignarMisiones(ViajeCreationWithMisionesDTO viajeDTO) {
        // 1. Inicialización del nuevo Viaje
        Viaje nuevoViaje = new Viaje();
        nuevoViaje.setTiempoInicio(viajeDTO.getTiempoInicio());
        nuevoViaje.setTiempoFin(viajeDTO.getTiempoFin());
        nuevoViaje.setDistanciaRecorrida(viajeDTO.getDistanciaRecorrida());

        // 2. Validación y asignación de EstadoViaje
        if (viajeDTO.getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viajeDTO.getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viajeDTO.getIdEstadoViaje() + " no existe.");
            }
            nuevoViaje.setEstadoViaje(existingEstadoViaje.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // 3. Validación y asignación de Vehiculo
        Vehiculo vehiculoSeleccionado = null;
        if (viajeDTO.getIdVehiculo() != null) {
            Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDTO.getIdVehiculo());
            if (existingVehiculo.isEmpty()) {
                throw new RuntimeException("El Vehiculo con ID " + viajeDTO.getIdVehiculo() + " no existe.");
            }
            vehiculoSeleccionado = existingVehiculo.get();
            nuevoViaje.setVehiculo(vehiculoSeleccionado);
        } else {
            throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
        }

        // --- CORRECCIÓN: Declarar placaVehiculo aquí para que sea accesible en todo el método ---
        String placaVehiculo = vehiculoSeleccionado.getPlaca();

        // --- VALIDACIÓN 1: Un vehículo no puede hacer dos viajes en la misma fecha ---
        // Se busca si ya existe un viaje para este vehículo que se solape con las fechas del nuevo viaje.
        List<Viaje> viajesExistentesParaVehiculo = viajeRepository.findByVehiculoAndTiempoFinAfterAndTiempoInicioBefore(
                vehiculoSeleccionado, nuevoViaje.getTiempoInicio(), nuevoViaje.getTiempoFin()
        );

        if (!viajesExistentesParaVehiculo.isEmpty()) {
            // Si se encuentra algún viaje que se solapa, se lanza una excepción.
            throw new RuntimeException(
                    "El vehículo con placa " + placaVehiculo + " ya está programado para otro viaje en el rango de fechas proporcionado."
            );
        }
        // --- FIN DE VALIDACIÓN 1 ---

        // 4. Determinación de la capacidad máxima del vehículo
        Pattern patronCarro = Pattern.compile("^[A-Z]{3}[0-9]{3}$"); // Formato AAA123
        Pattern patronMoto = Pattern.compile("^[A-Z]{3}[0-9]{2}[A-Z]$"); // Formato AAA23A

        Matcher matcherCarro = patronCarro.matcher(placaVehiculo);
        Matcher matcherMoto = patronMoto.matcher(placaVehiculo);

        int capacidadMaximaPasajeros; // Representa personas ADICIONALES al conductor
        String tipoVehiculo;

        if (matcherCarro.matches()) {
            capacidadMaximaPasajeros = 4; // Un carro puede llevar 4 pasajeros adicionales (total 5 con conductor)
            tipoVehiculo = "carro";
        } else if (matcherMoto.matches()) {
            capacidadMaximaPasajeros = 2; // Una moto puede llevar 2 pasajero adicional
            tipoVehiculo = "moto";
        } else {
            throw new RuntimeException("El formato de la placa '" + placaVehiculo + "' del vehículo no es reconocido (no es carro ni moto).");
        }

        // 5. Procesar Misiones para determinar la asignación según la capacidad
        Set<Integer> misionesAsignadasIds = new HashSet<>(); // IDs de misiones que SÍ se asignarán a este viaje
        List<Integer> misionesNoAsignadasIds = new ArrayList<>(); // IDs de misiones que NO se pudieron asignar
        List<Mision> misionesValidasParaAsignar = new ArrayList<>(); // Objetos Mision validados que se procesarán

        if (viajeDTO.getIdMisiones() != null && !viajeDTO.getIdMisiones().isEmpty()) {
            for (Integer idMision : viajeDTO.getIdMisiones()) {
                Optional<Mision> misionOptional = misionRepository.findById(idMision); // Asumiendo que numero_mision es el ID de Mision
                if (misionOptional.isPresent()) {
                    Mision mision = misionOptional.get();

                    // --- VALIDACIÓN 2: Verificar si la misión ya está programada en otro viaje ---
                    // Buscamos si ya existe una entrada en mision_x_viaje para esta misión
                    Optional<MisionXViaje> existingMisionXViaje = misionXViajeRepository.findByMision(mision);

                    if (existingMisionXViaje.isPresent()) {
                        // Si la misión ya está programada en otro viaje, la añadimos a las no asignadas
                        misionesNoAsignadasIds.add(mision.getNumeroMision());
                        System.out.println("ADVERTENCIA: La misión con ID " + mision.getNumeroMision() + " ya está programada en el viaje con ID " + existingMisionXViaje.get().getViaje().getIdViaje() + ". No se asignará a este nuevo viaje.");
                        continue; // Pasamos a la siguiente misión
                    }
                    // --- FIN DE VALIDACIÓN 2 ---

                    // Si todavía hay capacidad en el vehículo para más personas (misiones)
                    if (misionesAsignadasIds.size() < capacidadMaximaPasajeros) {
                        misionesAsignadasIds.add(mision.getNumeroMision()); // Añadir la misión a las asignadas
                        misionesValidasParaAsignar.add(mision); // Guardar el objeto Mision para usarlo después
                    } else {
                        // No hay más capacidad, esta misión no se puede asignar en este viaje
                        misionesNoAsignadasIds.add(mision.getNumeroMision());
                    }
                } else {
                    // Si una misión no existe, es un error grave que impide la creación del viaje.
                    throw new RuntimeException("Misión con ID " + idMision + " no encontrada. No se puede proceder con la creación del viaje.");
                }
            }
        }

        // 6. Preparar mensaje adicional si hay misiones no asignadas
        String mensajeAdicional = null;
        if (!misionesNoAsignadasIds.isEmpty()) {
            // Contar las misiones no asignadas por capacidad vs. ya programadas
            long misionesPorCapacidad = misionesNoAsignadasIds.stream()
                    .filter(id -> !misionesAsignadasIds.contains(id)) // Misiones que no se asignaron por capacidad
                    .count();
            long misionesYaProgramadas = misionesNoAsignadasIds.size() - misionesPorCapacidad;

            StringBuilder sb = new StringBuilder();
            if (misionesPorCapacidad > 0) {
                sb.append(String.format(
                        "El vehículo tipo %s (placa %s) solo puede transportar a %d personas. %d misiones excedieron la capacidad. ",
                        tipoVehiculo, placaVehiculo, capacidadMaximaPasajeros, misionesPorCapacidad
                ));
            }
            if (misionesYaProgramadas > 0) {
                sb.append(String.format(
                        "%d misiones ya estaban programadas en otros viajes. ",
                        misionesYaProgramadas
                ));
            }
            sb.append(String.format("Las misiones con IDs [%s] no fueron asignadas a este viaje. Por favor, gestione estas misiones en otro viaje.",
                    misionesNoAsignadasIds.stream().map(Object::toString).collect(Collectors.joining(", "))
            ));
            mensajeAdicional = sb.toString();
            System.out.println(mensajeAdicional); // También podrías usar un logger (ej. Slf4j)
        }

        // 7. Calcular viático y establecer activo
        nuevoViaje.setViatico(calcularViatico(nuevoViaje.getDistanciaRecorrida()));
        nuevoViaje.setActivo(true); // Asegurarse de que un nuevo viaje siempre se cree como activo

        // 8. Guardar el nuevo Viaje en la base de datos
        Viaje viajeGuardado = viajeRepository.save(nuevoViaje);

        // 9. Asignación REAL de Misiones al Viaje a través de MisionXViaje
        if (!misionesValidasParaAsignar.isEmpty()) {
            for (Mision mision : misionesValidasParaAsignar) {
                // Solo creamos la relación si la misión fue efectivamente asignada por capacidad
                if (misionesAsignadasIds.contains(mision.getNumeroMision())) {
                    MisionXViaje misionXViaje = new MisionXViaje(viajeGuardado, mision); // Crea una nueva instancia de MisionXViaje
                    misionXViajeRepository.save(misionXViaje); // Persiste la relación en la tabla mision_x_viaje
                }
            }
        }

        // 10. Retornar el resultado encapsulado en el DTO
        return new ViajeCreationResultDTO(viajeGuardado, misionesNoAsignadasIds, mensajeAdicional);
    }
    @Transactional // Garantiza que todas las operaciones de base de datos se manejen como una única transacción
    public ViajeCreationResultDTO actualizarViajeYAsignarMisiones(Integer idViaje, ViajeCreationWithMisionesDTO viajeDTO) {
        // 1. Recuperar el Viaje existente
        Optional<Viaje> existingViajeOptional = viajeRepository.findById(idViaje);
        if (existingViajeOptional.isEmpty()) {
            throw new RuntimeException("El Viaje con ID " + idViaje + " no existe.");
        }
        Viaje viajeExistente = existingViajeOptional.get();

        // 2. Actualizar los detalles básicos del Viaje
        viajeExistente.setTiempoInicio(viajeDTO.getTiempoInicio());
        viajeExistente.setTiempoFin(viajeDTO.getTiempoFin());
        viajeExistente.setDistanciaRecorrida(viajeDTO.getDistanciaRecorrida());

        // 3. Validar y asignar el EstadoViaje
        if (viajeDTO.getIdEstadoViaje() != null) {
            Optional<EstadoViaje> existingEstadoViaje = estadoViajeRepository.findById(viajeDTO.getIdEstadoViaje());
            if (existingEstadoViaje.isEmpty()) {
                throw new RuntimeException("El EstadoViaje con ID " + viajeDTO.getIdEstadoViaje() + " no existe.");
            }
            viajeExistente.setEstadoViaje(existingEstadoViaje.get());
        } else {
            throw new RuntimeException("Se debe proporcionar un EstadoViaje para el viaje.");
        }

        // 4. Validar y asignar el Vehículo
        Vehiculo vehiculoSeleccionado = null;
        if (viajeDTO.getIdVehiculo() != null) {
            Optional<Vehiculo> existingVehiculo = vehiculoRepository.findById(viajeDTO.getIdVehiculo());
            if (existingVehiculo.isEmpty()) {
                throw new RuntimeException("El Vehiculo con ID " + viajeDTO.getIdVehiculo() + " no existe.");
            }
            vehiculoSeleccionado = existingVehiculo.get();
            viajeExistente.setVehiculo(vehiculoSeleccionado);
        } else {
            throw new RuntimeException("Se debe proporcionar un Vehiculo para el viaje.");
        }

        String placaVehiculo = vehiculoSeleccionado.getPlaca();

        // --- VALIDACIÓN 1: Un vehículo no puede hacer dos viajes en la misma fecha (excluyendo el viaje actual) ---
        // Busca si ya existe un viaje para este vehículo que se solape con las fechas del viaje a actualizar,
        // pero excluye el viaje que estamos actualizando.
        List<Viaje> viajesExistentesParaVehiculo = viajeRepository.findByVehiculoAndTiempoFinAfterAndTiempoInicioBefore(
                vehiculoSeleccionado, viajeExistente.getTiempoInicio(), viajeExistente.getTiempoFin()
        );

        // Si se encuentra algún viaje que se solapa y no es el viaje que estamos actualizando, se lanza una excepción.
        if (viajesExistentesParaVehiculo.stream().anyMatch(v -> !v.getIdViaje().equals(idViaje))) {
            throw new RuntimeException(
                    "El vehículo con placa " + placaVehiculo + " ya está programado para otro viaje en el rango de fechas proporcionado."
            );
        }
        // --- FIN DE VALIDACIÓN 1 ---

        // 5. Determinar la capacidad máxima del vehículo
        Pattern patronCarro = Pattern.compile("^[A-Z]{3}[0-9]{3}$"); // Formato AAA123
        Pattern patronMoto = Pattern.compile("^[A-Z]{3}[0-9]{2}[A-Z]$"); // Formato AAA23A

        Matcher matcherCarro = patronCarro.matcher(placaVehiculo);
        Matcher matcherMoto = patronMoto.matcher(placaVehiculo);

        int capacidadMaximaPasajeros; // Representa personas ADICIONALES al conductor
        String tipoVehiculo;

        if (matcherCarro.matches()) {
            capacidadMaximaPasajeros = 4; // Un carro puede llevar 4 pasajeros adicionales (total 5 con conductor)
            tipoVehiculo = "carro";
        } else if (matcherMoto.matches()) {
            capacidadMaximaPasajeros = 2; // Una moto puede llevar 2 pasajeros adicionales
            tipoVehiculo = "moto";
        } else {
            throw new RuntimeException("El formato de la placa '" + placaVehiculo + "' del vehículo no es reconocido (no es carro ni moto).");
        }

        // 6. Procesar Misiones para determinar la asignación según la capacidad
        Set<Integer> misionesAsignadasIds = new HashSet<>(); // IDs de misiones que SÍ se asignarán a este viaje
        List<Integer> misionesNoAsignadasIds = new ArrayList<>(); // IDs de misiones que NO se pudieron asignar
        List<Mision> misionesValidasParaAsignar = new ArrayList<>(); // Objetos Mision validados que se procesarán

        // Primero, eliminamos las asociaciones de misiones existentes para este viaje.
        // Esto permite que el viaje actualice completamente sus misiones.
        misionXViajeRepository.deleteByViaje(viajeExistente);

        if (viajeDTO.getIdMisiones() != null && !viajeDTO.getIdMisiones().isEmpty()) {
            for (Integer idMision : viajeDTO.getIdMisiones()) {
                Optional<Mision> misionOptional = misionRepository.findById(idMision);
                if (misionOptional.isPresent()) {
                    Mision mision = misionOptional.get();

                    // --- VALIDACIÓN 2: Verificar si la misión ya está programada en otro viaje (excluyendo el viaje actual) ---
                    // Busca si ya existe una entrada en mision_x_viaje para esta misión.
                    Optional<MisionXViaje> existingMisionXViaje = misionXViajeRepository.findByMision(mision);

                    // Si la misión ya está programada en otro viaje Y NO ES el viaje que estamos actualizando,
                    // la añadimos a las no asignadas.
                    if (existingMisionXViaje.isPresent() && !existingMisionXViaje.get().getViaje().getIdViaje().equals(idViaje)) {
                        misionesNoAsignadasIds.add(mision.getNumeroMision());
                        System.out.println("ADVERTENCIA: La misión con ID " + mision.getNumeroMision() + " ya está programada en el viaje con ID " + existingMisionXViaje.get().getViaje().getIdViaje() + ". No se asignará a este viaje.");
                        continue; // Pasamos a la siguiente misión
                    }
                    // --- FIN DE VALIDACIÓN 2 ---

                    // Si todavía hay capacidad en el vehículo para más personas (misiones)
                    if (misionesAsignadasIds.size() < capacidadMaximaPasajeros) {
                        misionesAsignadasIds.add(mision.getNumeroMision()); // Añadir la misión a las asignadas
                        misionesValidasParaAsignar.add(mision); // Guardar el objeto Mision para usarlo después
                    } else {
                        // No hay más capacidad, esta misión no se puede asignar en este viaje
                        misionesNoAsignadasIds.add(mision.getNumeroMision());
                    }
                } else {
                    // Si una misión no existe, es un error grave que impide la actualización del viaje.
                    throw new RuntimeException("Misión con ID " + idMision + " no encontrada. No se puede proceder con la actualización del viaje.");
                }
            }
        }

        // 7. Preparar mensaje adicional si hay misiones no asignadas
        String mensajeAdicional = null;
        if (!misionesNoAsignadasIds.isEmpty()) {
            // Contar las misiones no asignadas por capacidad vs. ya programadas
            long misionesPorCapacidad = misionesNoAsignadasIds.stream()
                    .filter(id -> !misionesAsignadasIds.contains(id)) // Misiones que no se asignaron por capacidad
                    .count();
            long misionesYaProgramadas = misionesNoAsignadasIds.size() - misionesPorCapacidad;

            StringBuilder sb = new StringBuilder();
            if (misionesPorCapacidad > 0) {
                sb.append(String.format(
                        "El vehículo tipo %s (placa %s) solo puede transportar a %d personas. %d misiones excedieron la capacidad. ",
                        tipoVehiculo, placaVehiculo, capacidadMaximaPasajeros, misionesPorCapacidad
                ));
            }
            if (misionesYaProgramadas > 0) {
                sb.append(String.format(
                        "%d misiones ya estaban programadas en otros viajes. ",
                        misionesYaProgramadas
                ));
            }
            sb.append(String.format("Las misiones con IDs [%s] no fueron asignadas a este viaje. Por favor, gestione estas misiones en otro viaje.",
                    misionesNoAsignadasIds.stream().map(Object::toString).collect(Collectors.joining(", "))
            ));
            mensajeAdicional = sb.toString();
            System.out.println(mensajeAdicional);
        }

        // 8. Calcular viático y establecer el estado activo
        viajeExistente.setViatico(calcularViatico(viajeExistente.getDistanciaRecorrida()));
        // Aquí podrías añadir lógica para actualizar el estado 'activo' si el DTO lo permite:
        // viajeExistente.setActivo(viajeDTO.isActivo());

        // 9. Guardar el Viaje actualizado en la base de datos
        Viaje viajeActualizado = viajeRepository.save(viajeExistente);

        // 10. Asignación REAL de Misiones al Viaje a través de MisionXViaje
        if (!misionesValidasParaAsignar.isEmpty()) {
            for (Mision mision : misionesValidasParaAsignar) {
                // Solo creamos la relación si la misión fue efectivamente asignada por capacidad
                if (misionesAsignadasIds.contains(mision.getNumeroMision())) {
                    MisionXViaje misionXViaje = new MisionXViaje(viajeActualizado, mision); // Crea una nueva instancia
                    misionXViajeRepository.save(misionXViaje); // Persiste la relación
                }
            }
        }

        // 11. Retornar el resultado encapsulado en el DTO
        return new ViajeCreationResultDTO(viajeActualizado, misionesNoAsignadasIds, mensajeAdicional);
    }

    /**
     * Obtiene una lista de DTOs de misiones asociadas a un viaje específico.
     * Utiliza la tabla de unión MisionXViaje para encontrar las misiones y luego las mapea a MisionDTO.
     *
     * @param idViaje El ID del viaje.
     * @return Una lista de objetos MisionDTO.
     * @throws RuntimeException Si el viaje con el ID proporcionado no existe.
     */
    @Transactional(readOnly = true)
    public List<MisionDTO> getMisionesByViajeId(Integer idViaje) {
        logger.info("Intentando obtener misiones para el viaje con ID: {}", idViaje);
        // 1. Validar que el viaje exista (opcional, pero buena práctica)
        Optional<Viaje> viajeOptional = viajeRepository.findById(idViaje);
        if (viajeOptional.isEmpty()) {
            logger.warn("Viaje con ID {} no encontrado al intentar obtener misiones.", idViaje);
            throw new RuntimeException("El viaje con ID " + idViaje + " no existe.");
        }
        logger.debug("Viaje con ID {} encontrado.", idViaje);

        // 2. Obtener todas las relaciones MisionXViaje para este viaje
        List<MisionXViaje> misionesXViaje = misionXViajeRepository.findByViaje_IdViaje(idViaje);

        if (misionesXViaje.isEmpty()) {
            logger.info("No se encontraron misiones asociadas al viaje con ID: {}", idViaje);
            return Collections.emptyList(); // Devuelve una lista vacía si no hay misiones
        }

        // 3. Mapear las entidades Mision a MisionDTO
        List<MisionDTO> misionDTOs = misionesXViaje.stream()
                .map(MisionXViaje::getMision)
                .filter(Objects::nonNull) // Asegurarse de que la misión no sea nula (aunque no debería pasar con el findBy)
                .map(this::convertToMisionDTO)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} misiones para el viaje con ID: {}", misionDTOs.size(), idViaje);
        return misionDTOs;
    }
    /**
     * Método auxiliar para convertir una entidad Mision a un MisionDTO.
     * Recupera información adicional de las entidades relacionadas si están presentes.
     *
     * @param mision La entidad Mision a convertir.
     * @return El MisionDTO correspondiente.
     */
    private MisionDTO convertToMisionDTO(Mision mision) {
        MisionDTO dto = new MisionDTO();
        dto.setNumeroMision(mision.getNumeroMision());
        dto.setActividades(mision.getActividades());
        dto.setActivo(mision.getActivo());

        // Manejar Funcionario
        if (mision.getFuncionario() != null) {
            // Se asume que la entidad Mision ya tiene el objeto Funcionario cargado
            // Si no es así (Lazy Loading), necesitarías cargarlo explícitamente desde funcionarioRepository
            dto.setIdFuncionario(mision.getFuncionario().getIdFuncionario());
            dto.setNombreFuncionario(mision.getFuncionario().getNombre());
            dto.setApellidoFuncionario(mision.getFuncionario().getApellido());
        }

        // Manejar Caso
        if (mision.getCaso() != null) {
            // Se asume que la entidad Mision ya tiene el objeto Caso cargado
            // Si no es así (Lazy Loading), necesitarías cargarlo explícitamente desde casoRepository
            dto.setIdCaso(mision.getCaso().getIdCaso());
            dto.setNumeroCaso(mision.getCaso().getCodigoCaso()); // Asumiendo que Caso tiene getCodigoCaso()
        }

        // Manejar Especialidad
        if (mision.getEspecialidad() != null) {
            // Se asume que la entidad Mision ya tiene el objeto Especialidad cargado
            // Si no es así (Lazy Loading), necesitarías cargarlo explícitamente desde especialidadRepository
            dto.setIdEspecialidad(mision.getEspecialidad().getIdEspecialidad());
            dto.setEspecialidad(mision.getEspecialidad().getEspecialidad()); // Asumiendo que Especialidad tiene getNombreEspecialidad()
        }

        return dto;
    }
}