package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Delito;
import com.agendamientos.agendamientosTurnos.service.DelitoService;
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
@RequestMapping("/api/delitos")
@Tag(name = "Delitos", description = "API para la gestión de delitos")
public class DelitoController {

    private final DelitoService delitoService;

    @Autowired
    public DelitoController(DelitoService delitoService) {
        this.delitoService = delitoService;
    }

    @Operation(summary = "Obtener todos los delitos", description = "Retorna una lista de todos los delitos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Delito.class)))
    @GetMapping
    public ResponseEntity<List<Delito>> getAllDelitos() {
        List<Delito> delitos = delitoService.getAllDelitos();
        return new ResponseEntity<>(delitos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un delito por ID", description = "Devuelve un delito específico dado su ID.")
    @ApiResponse(responseCode = "200", description = "Delito encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Delito.class)))
    @ApiResponse(responseCode = "404", description = "Delito no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Delito> getDelitoById(
            @Parameter(description = "ID del delito", example = "1") @PathVariable int id) {
        Optional<Delito> delito = delitoService.getDelitoById(id);
        return delito.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo delito", description = "Registra un nuevo delito en el sistema.")
    @ApiResponse(responseCode = "201", description = "Delito creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Delito.class)))
    @PostMapping
    public ResponseEntity<Delito> createDelito(
            @Parameter(description = "Datos del delito a registrar") @RequestBody Delito delito) {
        Delito savedDelito = delitoService.saveOrUpdateDelito(delito);
        return new ResponseEntity<>(savedDelito, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un delito existente", description = "Modifica los datos de un delito existente.")
    @ApiResponse(responseCode = "200", description = "Delito actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Delito.class)))
    @ApiResponse(responseCode = "404", description = "Delito no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Delito> updateDelito(
            @Parameter(description = "ID del delito a actualizar", example = "1") @PathVariable int id,
            @Parameter(description = "Datos actualizados del delito") @RequestBody Delito delito) {
        Optional<Delito> existingDelito = delitoService.getDelitoById(id);
        if (existingDelito.isPresent()) {
            delito.setIdDelito(id); // Aseguramos que el ID coincida
            Delito updatedDelito = delitoService.saveOrUpdateDelito(delito);
            return new ResponseEntity<>(updatedDelito, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un delito", description = "Elimina un delito del sistema dado su ID.")
    @ApiResponse(responseCode = "204", description = "Delito eliminado exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelito(
            @Parameter(description = "ID del delito a eliminar", example = "1") @PathVariable int id) {
        delitoService.deleteDelitoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
