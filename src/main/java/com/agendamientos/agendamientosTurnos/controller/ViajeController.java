package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/viajes") // Define la ruta base para todos los endpoints de este controlador
public class ViajeController {

    @Autowired // Inyecta una instancia de ViajeService
    private ViajeService viajeService;

    // Endpoint para obtener todos los viajes
    @GetMapping // GET /api/viajes
    public ResponseEntity<List<Viaje>> getAllViajes() {
        List<Viaje> viajes = viajeService.findAll();
        return new ResponseEntity<>(viajes, HttpStatus.OK);
    }

    // Endpoint para obtener un viaje por ID
    @GetMapping("/{id}") // GET /api/viajes/{id}
    public ResponseEntity<Viaje> getViajeById(@PathVariable Integer id) {
        Optional<Viaje> viaje = viajeService.findById(id);
        return viaje.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    // Endpoint para eliminar un viaje por ID
    @DeleteMapping("/{id}") // DELETE /api/viajes/{id}
    public ResponseEntity<Void> deleteViaje(@PathVariable Integer id) {
        try {
            viajeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content indica éxito sin cuerpo de respuesta
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si el viaje no existe
        }
    }


    @PostMapping
    public ResponseEntity<Viaje> createViaje(@Valid @RequestBody Viaje viaje) {
        try {
            // El campo 'viatico' se calculará automáticamente en el servicio.
            // El cliente debe enviar 'distanciaRecorrida'.
            Viaje newViaje = viajeService.save(viaje);
            return new ResponseEntity<>(newViaje, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Aquí puedes mejorar el manejo de errores para dar más detalle.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> updateViaje(@PathVariable Integer id, @Valid @RequestBody Viaje viajeDetails) {
        try {
            // El campo 'viatico' se recalculará automáticamente en el servicio.
            // El cliente debe enviar 'distanciaRecorrida' actualizada.
            Viaje updatedViaje = viajeService.update(id, viajeDetails);
            return new ResponseEntity<>(updatedViaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }
}