package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.service.EstadoViajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estado-viajes")
@Tag(name = "Estados de Viaje", description = "Operaciones relacionadas con los estados de viaje")
public class EstadoViajeController {

    @Autowired
    private EstadoViajeService estadoViajeService;

    @Operation(summary = "Listar todos los estados de viaje", description = "Devuelve todos los estados de viaje registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoViaje.class)))
    @GetMapping
    public ResponseEntity<List<EstadoViaje>> getAllEstadoViajes() {
        List<EstadoViaje> estadoViajes = estadoViajeService.findAll();
        return new ResponseEntity<>(estadoViajes, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estado de viaje por ID", description = "Devuelve un estado de viaje específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Estado de viaje encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoViaje.class)))
    @ApiResponse(responseCode = "404", description = "Estado de viaje no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoViaje> getEstadoViajeById(
            @Parameter(description = "ID del estado de viaje", example = "1") @PathVariable Integer id) {
        Optional<EstadoViaje> estadoViaje = estadoViajeService.findById(id);
        return estadoViaje.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo estado de viaje", description = "Registra un nuevo estado de viaje en el sistema.")
    @ApiResponse(responseCode = "201", description = "Estado de viaje creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoViaje.class)))
    @PostMapping
    public ResponseEntity<EstadoViaje> createEstadoViaje(
            @Valid @RequestBody EstadoViaje estadoViaje) {
        EstadoViaje newEstadoViaje = estadoViajeService.save(estadoViaje);
        return new ResponseEntity<>(newEstadoViaje, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar estado de viaje", description = "Modifica los datos de un estado de viaje existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Estado de viaje actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoViaje.class)))
    @ApiResponse(responseCode = "404", description = "Estado de viaje no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EstadoViaje> updateEstadoViaje(
            @Parameter(description = "ID del estado de viaje", example = "1") @PathVariable Integer id,
            @Valid @RequestBody EstadoViaje estadoViajeDetails) {
        try {
            EstadoViaje updatedEstadoViaje = estadoViajeService.update(id, estadoViajeDetails);
            return new ResponseEntity<>(updatedEstadoViaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar estado de viaje", description = "Elimina un estado de viaje del sistema por su ID.")
    @ApiResponse(responseCode = "204", description = "Estado eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Estado de viaje no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoViaje(
            @Parameter(description = "ID del estado a eliminar", example = "1") @PathVariable Integer id) {
        Optional<EstadoViaje> estadoViaje = estadoViajeService.findById(id);
        if (estadoViaje.isPresent()) {
            estadoViajeService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
