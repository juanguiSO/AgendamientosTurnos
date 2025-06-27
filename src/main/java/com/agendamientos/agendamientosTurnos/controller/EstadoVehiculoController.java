package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.EstadoVehiculo;
import com.agendamientos.agendamientosTurnos.service.EstadoVehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estado-vehiculos")
@Tag(name = "Estado de Vehículos", description = "API para la gestión del estado de los vehículos")
public class EstadoVehiculoController {

    @Autowired
    private EstadoVehiculoService estadoVehiculoService;

    @Operation(summary = "Obtener todos los estados de vehículos", description = "Devuelve una lista con todos los estados registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoVehiculo.class)))
    @GetMapping
    public ResponseEntity<List<EstadoVehiculo>> getAllEstadoVehiculos() {
        List<EstadoVehiculo> estadoVehiculos = estadoVehiculoService.getAllEstadoVehiculos();
        return new ResponseEntity<>(estadoVehiculos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener estado de vehículo por ID", description = "Devuelve el estado de un vehículo específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Estado encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoVehiculo.class)))
    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EstadoVehiculo> getEstadoVehiculoById(
            @Parameter(description = "ID del estado del vehículo", example = "1") @PathVariable Integer id) {
        Optional<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.getEstadoVehiculoById(id);
        return estadoVehiculo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo estado de vehículo", description = "Crea y registra un nuevo estado de vehículo.")
    @ApiResponse(responseCode = "201", description = "Estado creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoVehiculo.class)))
    @PostMapping
    public ResponseEntity<EstadoVehiculo> createEstadoVehiculo(
            @RequestBody EstadoVehiculo estadoVehiculo) {
        EstadoVehiculo createdEstadoVehiculo = estadoVehiculoService.createEstadoVehiculo(estadoVehiculo);
        return new ResponseEntity<>(createdEstadoVehiculo, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un estado de vehículo", description = "Actualiza un estado de vehículo existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadoVehiculo.class)))
    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EstadoVehiculo> updateEstadoVehiculo(
            @Parameter(description = "ID del estado del vehículo a actualizar", example = "1") @PathVariable Integer id,
            @RequestBody EstadoVehiculo estadoVehiculo) {
        EstadoVehiculo updatedEstadoVehiculo = estadoVehiculoService.updateEstadoVehiculo(id, estadoVehiculo);
        if (updatedEstadoVehiculo != null) {
            return new ResponseEntity<>(updatedEstadoVehiculo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un estado de vehículo", description = "Elimina un estado de vehículo por su ID.")
    @ApiResponse(responseCode = "204", description = "Estado eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoVehiculo(
            @Parameter(description = "ID del estado a eliminar", example = "1") @PathVariable Integer id) {
        boolean deleted = estadoVehiculoService.deleteEstadoVehiculo(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
