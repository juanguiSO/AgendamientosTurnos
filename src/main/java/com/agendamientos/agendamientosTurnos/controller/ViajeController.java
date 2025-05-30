package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.ViajeCreationDTO; // Import the DTO for trip creation with cases
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.service.ViajeService;
import jakarta.validation.Valid; // Keep if you're still validating Viaje entity directly
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import for the payload of case assignment
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
     * Endpoint to create a new trip and assign it to existing cases.
     * Uses ViajeCreationDTO to receive trip details and a list of case IDs.
     * POST /api/viajes/crear-con-casos
     *
     * @param viajeDTO The DTO containing trip details and case IDs.
     * @return ResponseEntity with the created Viaje object or an error status.
     */
    @PostMapping("/crear-con-casos") // New endpoint for creating a trip with case assignments
    public ResponseEntity<Viaje> crearViajeConCasos(@Valid @RequestBody ViajeCreationDTO viajeDTO) {
        try {
            Viaje newViaje = viajeService.crearViajeYAsignarCasos(viajeDTO);
            return new ResponseEntity<>(newViaje, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error creating trip with cases: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Original endpoint for creating a trip (without explicit case assignment at creation)
     * You might choose to keep this if you have scenarios where a trip is created
     * without immediate case assignment, or remove it if 'crearViajeConCasos'
     * is the primary way to create trips.
     * POST /api/viajes
     *
     * @param viaje The Viaje object to create.
     * @return ResponseEntity with the created Viaje object or an error status.
     */
    @PostMapping // POST /api/viajes
    public ResponseEntity<Viaje> createViaje(@Valid @RequestBody Viaje viaje) {
        try {
            // The 'viatico' field will be automatically calculated in the service.
            // The client must send 'distanciaRecorrida'.
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or a more specific error message
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
}
