package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.ViajeCreationDTO; // Import the DTO for trip creation with cases
import com.agendamientos.agendamientosTurnos.dto.ViajeCreationResultDTO;
import com.agendamientos.agendamientosTurnos.dto.ViajeCreationWithMisionesDTO;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Indicates that this class is a REST controller
@RequestMapping("/api/viajes") // Defines the base path for all endpoints in this controller
public class ViajeController {

    @Autowired // Injects an instance of ViajeService
    private ViajeService viajeService;

    /**
     * Endpoint to get all active trips.
     * GET /api/viajes
     *
     * @return ResponseEntity with a list of active Viaje objects.
     */
    @GetMapping // GET /api/viajes
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
    @GetMapping("/{id}") // GET /api/viajes/{id}
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
    @DeleteMapping("/{id}") // DELETE /api/viajes/{id}
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
    @PostMapping("/crear-con-casos") // Endpoint para crear un viaje con asignación de casos (YA ERA ÚNICO)
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
    @PostMapping("/simple-creation") // <--- CAMBIO AQUÍ: Ruta única para evitar conflicto
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
    @PostMapping("/con-misiones") // <--- CAMBIO AQUÍ: Ruta única para evitar conflicto
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
    @PutMapping("/{idViaje}/misiones") // Mapea las peticiones PUT a /api/viajes/{idViaje}
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
}
