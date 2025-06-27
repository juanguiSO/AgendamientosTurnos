package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.*;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.service.ViajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/viajes")
@Tag(name = "Gestión de Viajes", description = "Operaciones relacionadas con viajes y sus misiones")
public class ViajeController {

    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    @Autowired // Injects an instance of ViajeService
    private ViajeService viajeService;

    /**
     * Endpoint to get all active trips.
     * GET /api/viajes
     *
     * @return ResponseEntity with a list of active Viaje objects.
     */
    @Operation(summary = "Obtener todos los viajes activos")
    @ApiResponse(responseCode = "200", description = "Lista de viajes obtenida", content = @Content(schema = @Schema(implementation = Viaje.class)))
    @GetMapping
    public ResponseEntity<List<Viaje>> getAllViajes() {
        // Ahora llama a findAll() en el servicio que ya filtra por 'activo'
        List<Viaje> viajes = viajeService.findAll();
        return new ResponseEntity<>(viajes, HttpStatus.OK);
    }

    /**
     * Endpoint to get a trip by ID, only if it's active.
     * GET /api/viajes/{id}
     *
     * @param id The ID of the trip.
     * @return ResponseEntity with the Viaje object if found and active, or NOT_FOUND.
     */
    @Operation(summary = "Obtener un viaje por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje encontrado", content = @Content(schema = @Schema(implementation = Viaje.class))),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Viaje> getViajeById(@PathVariable Integer id) {
        // Ahora llama a findById() en el servicio que ya filtra por 'activo'
        Optional<Viaje> viaje = viajeService.findById(id);
        return viaje.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to logically delete a trip by ID.
     * Sets the 'activo' field to false instead of physical deletion.
     * DELETE /api/viajes/{id}
     *
     * @param id The ID of the trip to logically delete.
     * @return ResponseEntity with NO_CONTENT on success, or NOT_FOUND/BAD_REQUEST on error.
     */
    @Operation(summary = "Eliminar lógicamente un viaje")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Viaje eliminado"),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado o inactivo"),
            @ApiResponse(responseCode = "400", description = "Error de solicitud")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViaje(@PathVariable Integer id) {
        try {
            // Llama al método deleteById() del servicio que ahora realiza el borrado lógico
            viajeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content indicates success without response body
        } catch (RuntimeException e) {
            System.err.println("Error deleting trip " + id + ": " + e.getMessage());
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("inactivo")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si el viaje no existe o ya está inactivo
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Otros errores
            }
        }
    }

    /**
     * Endpoint para crear un nuevo viaje y asignar casos, manejando la capacidad del vehículo.
     * Retorna un DTO con el viaje creado y la información sobre misiones no asignadas.
     *
     * @param viajeDTO DTO con los detalles del viaje y los IDs de los casos.
     * @return ResponseEntity con ViajeCreationResultDTO y el estado HTTP.
     */
    @Operation(summary = "Crear un viaje y asignar casos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Viaje creado"),
            @ApiResponse(responseCode = "202", description = "Viaje creado con casos no asignados"),
            @ApiResponse(responseCode = "400", description = "Error de datos")
    })
    @PostMapping("/crear-con-casos")// Endpoint para crear un viaje con asignación de casos (YA ERA ÚNICO)
    public ResponseEntity<ViajeCreationResultDTO> crearViajeConCasos(@Valid @RequestBody ViajeCreationDTO viajeDTO) {
        try {
            // Llama al método de servicio actualizado que devuelve ViajeCreationResultDTO
            ViajeCreationResultDTO resultDTO = viajeService.crearViajeYAsignarCasos(viajeDTO);

            if (resultDTO.getMisionesNoAsignadasIds() != null && !resultDTO.getMisionesNoAsignadasIds().isEmpty()) {
                return new ResponseEntity<>(resultDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(resultDTO, HttpStatus.CREATED);
            }

        } catch (RuntimeException e) {
            System.err.println("Error al crear viaje con casos: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Original endpoint for creating a trip (without explicit case assignment at creation)
     * This method now has a unique path to avoid ambiguity.
     * POST /api/viajes/simple-creation
     *
     * @param viaje The Viaje object to create.
     * @return ResponseEntity with the created Viaje object or an error status.
     */
    @Operation(summary = "Crear un viaje simple")
    @ApiResponse(responseCode = "201", description = "Viaje creado")
    @PostMapping("/simple-creation")// <--- CAMBIO AQUÍ: Ruta única para evitar conflicto
    public ResponseEntity<Viaje> createViaje(@Valid @RequestBody Viaje viaje) {
        try {
            Viaje newViaje = viajeService.save(viaje);
            return new ResponseEntity<>(newViaje, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error creating trip: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to update an existing trip.
     * Applies the logic for viatico calculation and validates relations with EstadoViaje and Vehiculo.
     * PUT /api/viajes/{id}
     *
     * @param id The ID of the trip to update.
     * @param viajeDetails The Viaje object with updated details.
     * @return ResponseEntity with the updated Viaje object or an error status.
     */

    @Operation(summary = "Actualizar un viaje existente")
    @PutMapping("/{id}")
    public ResponseEntity<Viaje> updateViaje(@PathVariable Integer id, @Valid @RequestBody Viaje viajeDetails) {
        try {
            Viaje updatedViaje = viajeService.update(id, viajeDetails);
            return new ResponseEntity<>(updatedViaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("Error updating trip " + id + ": " + e.getMessage());
            if (e.getMessage().contains("no encontrado")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Endpoint to assign or reassign a list of cases to an existing trip.
     * Cases that are no longer in the provided list will be unlinked from the trip.
     * PUT /api/viajes/{idViaje}/casos
     *
     * @param idViaje The ID of the trip to which cases will be assigned.
     * @param payload A map containing a list of 'idCasos' to be associated with this trip.
     * @return ResponseEntity with the updated Viaje object or an error status.
     */
    @Operation(summary = "Asignar casos a un viaje")
    @PutMapping("/{idViaje}/casos")
    public ResponseEntity<Viaje> asignarCasosAViaje(@PathVariable Integer idViaje, @RequestBody Map<String, List<Integer>> payload) {
        List<Integer> idCasos = payload.get("idCasos");
        if (idCasos == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Viaje viajeActualizado = viajeService.asignarCasosExistentesAViaje(idViaje, idCasos);
            return new ResponseEntity<>(viajeActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("Error assigning cases to trip " + idViaje + ": " + e.getMessage());
            if (e.getMessage().contains("no encontrado")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Endpoint para crear un nuevo viaje y asignarle misiones.
     * Recibe un ViajeCreationWithMisionesDTO en el cuerpo de la solicitud.
     * Mapea a POST /api/viajes/con-misiones
     *
     * @param viajeDTO El DTO que contiene los datos del viaje y las misiones a asignar.
     * @return ResponseEntity con ViajeCreationResultDTO y el estado HTTP CREATED (201) si es exitoso,
     * o un estado de error con un mensaje descriptivo.
     */
    @Operation(summary = "Crear un viaje con misiones")
    @PostMapping("/con-misiones")// <--- CAMBIO AQUÍ: Ruta única para evitar conflicto
    public ResponseEntity<ViajeCreationResultDTO> crearViajeYAsignarMisiones(
            @Valid @RequestBody ViajeCreationWithMisionesDTO viajeDTO) {
        try {
            ViajeCreationResultDTO result = viajeService.crearViajeYAsignarMisiones(viajeDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            ViajeCreationResultDTO errorResult = new ViajeCreationResultDTO(null, null, e.getMessage());
            return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para actualizar un viaje existente y sus misiones asociadas.
     * Utiliza un PUT para actualizaciones completas o PATCH para parciales, aquí se usa PUT.
     * @param idViaje El ID del viaje a actualizar, recibido como parte de la URL.
     * @param viajeDTO Los datos del viaje y misiones a actualizar, recibidos en el cuerpo de la petición.
     * @return ResponseEntity con el resultado de la operación o un mensaje de error.
     */
    @Operation(summary = "Actualizar viaje con misiones")
    @PutMapping("/{idViaje}/misiones") // Mapea las peticiones PUT a /api/viajes/{idViaje}/misiones
    public ResponseEntity<ViajeCreationResultDTO> actualizarViaje(
            @PathVariable Integer idViaje, // Captura el ID del viaje de la URL
            @RequestBody ViajeCreationWithMisionesDTO viajeDTO) { // Captura los datos del cuerpo de la petición
        try {
            // Llama al método de servicio para actualizar el viaje
            ViajeCreationResultDTO result = viajeService.actualizarViajeYAsignarMisiones(idViaje, viajeDTO);
            return new ResponseEntity<>(result, HttpStatus.OK); // Retorna el resultado con estado 200 OK

        } catch (RuntimeException e) {
            // Manejo básico de errores: si ocurre una excepción, retorna un 400 Bad Request
            // En una aplicación real, usarías un manejo de excepciones más sofisticado (Ej. @ControllerAdvice)
            System.err.println("Error al actualizar viaje: " + e.getMessage()); // Imprime el error para depuración
            // Puedes crear un DTO de error más específico si lo necesitas
            return new ResponseEntity<>(new ViajeCreationResultDTO(null, null, "Error: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para obtener todas las misiones asociadas a un viaje específico.
     * GET /api/viajes/{idViaje}/misiones
     *
     * @param idViaje El ID del viaje del cual se quieren obtener las misiones.
     * @return ResponseEntity con una lista de MisionDTOs o un estado NOT_FOUND/BAD_REQUEST.
     */
    @Operation(summary = "Obtener misiones asociadas a un viaje")
    @GetMapping("/{idViaje}/misiones")
    public ResponseEntity<?> getMisionesByViajeId(@PathVariable Integer idViaje) {
        // Log: Indica que se ha recibido una petición para este endpoint con el ID de viaje.
        logger.info("Petición recibida para obtener misiones del viaje con ID: {}", idViaje);
        try {
            List<MisionDTO> misiones = viajeService.getMisionesByViajeId(idViaje);

            if (misiones.isEmpty()) {
                // Log: Informa que el viaje existe pero no tiene misiones asociadas.
                logger.info("Viaje con ID {} existe, pero no tiene misiones asociadas. Retornando 204 No Content.", idViaje);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
            }
            // Log: Informa la cantidad de misiones encontradas para el viaje.
            logger.info("Misiones obtenidas exitosamente para el viaje con ID {}. Cantidad: {}", idViaje, misiones.size());

            // Opcional: Si quieres ver los detalles de las misiones en el log (puede ser muy verboso para muchas misiones)
            // Esto solo se activará si el nivel de log es DEBUG
            logger.debug("Detalle de misiones para el viaje {}: {}", idViaje, misiones);

            return new ResponseEntity<>(misiones, HttpStatus.OK);

        } catch (RuntimeException e) {
            String errorMessage = "Error al obtener misiones para el viaje " + idViaje + ": " + e.getMessage();
            // Log: Registra el error completo, incluyendo la pila de llamadas (stack trace).
            logger.error(errorMessage, e);

            if (e.getMessage().contains("no existe")) {
                // Log: Detalle específico si el viaje no fue encontrado por el servicio.
                logger.warn("El viaje con ID {} no fue encontrado. Retornando 404 Not Found.", idViaje);
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.NOT_FOUND,
                        "Viaje no encontrado: " + e.getMessage(), // Mensaje más específico
                        "/api/viajes/" + idViaje + "/misiones"
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404 Not Found
            } else {
                // Log: Detalle para otros tipos de errores.
                logger.error("Error inesperado al procesar la solicitud para el viaje {}. Retornando 400 Bad Request.", idViaje);
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "Solicitud inválida: " + e.getMessage(),
                        "/api/viajes/" + idViaje + "/misiones"
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400 Bad Request
            }
        }
    }
}