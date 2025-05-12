package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.service.EstadoViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/estado-viajes") // Define la ruta base para todos los endpoints de este controlador
public class EstadoViajeController {

    @Autowired // Inyecta una instancia de EstadoViajeService
    private EstadoViajeService estadoViajeService;

    // Endpoint para obtener todos los estados de viaje
    @GetMapping // GET /api/estado-viajes
    public ResponseEntity<List<EstadoViaje>> getAllEstadoViajes() {
        List<EstadoViaje> estadoViajes = estadoViajeService.findAll();
        return new ResponseEntity<>(estadoViajes, HttpStatus.OK);
    }

    // Endpoint para obtener un estado de viaje por ID
    @GetMapping("/{id}") // GET /api/estado-viajes/{id}
    public ResponseEntity<EstadoViaje> getEstadoViajeById(@PathVariable Integer id) {
        Optional<EstadoViaje> estadoViaje = estadoViajeService.findById(id);
        return estadoViaje.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para crear un nuevo estado de viaje
    @PostMapping // POST /api/estado-viajes
    public ResponseEntity<EstadoViaje> createEstadoViaje(@Valid @RequestBody EstadoViaje estadoViaje) {
        // @Valid activa las validaciones de la entidad (ej. @NotBlank, @Size)
        EstadoViaje newEstadoViaje = estadoViajeService.save(estadoViaje);
        return new ResponseEntity<>(newEstadoViaje, HttpStatus.CREATED);
    }

    // Endpoint para actualizar un estado de viaje existente
    @PutMapping("/{id}") // PUT /api/estado-viajes/{id}
    public ResponseEntity<EstadoViaje> updateEstadoViaje(@PathVariable Integer id, @Valid @RequestBody EstadoViaje estadoViajeDetails) {
        try {
            EstadoViaje updatedEstadoViaje = estadoViajeService.update(id, estadoViajeDetails);
            return new ResponseEntity<>(updatedEstadoViaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar un estado de viaje por ID
    @DeleteMapping("/{id}") // DELETE /api/estado-viajes/{id}
    public ResponseEntity<Void> deleteEstadoViaje(@PathVariable Integer id) {
        Optional<EstadoViaje> estadoViaje = estadoViajeService.findById(id);
        if (estadoViaje.isPresent()) {
            estadoViajeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}