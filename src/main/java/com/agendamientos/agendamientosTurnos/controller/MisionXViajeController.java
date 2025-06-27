package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.MisionNumeroDTO;
import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.service.MisionXViajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/misionXViaje")
@Tag(name = "Misión x Viaje", description = "Operaciones para gestionar la relación entre misiones y viajes")
public class MisionXViajeController {

    private final MisionXViajeService service;

    public MisionXViajeController(MisionXViajeService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todas las relaciones Misión x Viaje")
    @ApiResponse(responseCode = "200", description = "Listado de relaciones recuperado correctamente")
    @GetMapping
    public ResponseEntity<List<MisionXViaje>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener una relación Misión x Viaje por ID")
    @ApiResponse(responseCode = "200", description = "Relación encontrada")
    @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<MisionXViaje> getById(
            @Parameter(description = "ID de la relación") @PathVariable Long id) {
        Optional<MisionXViaje> misionXViaje = service.findById(id);
        return misionXViaje.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva relación Misión x Viaje")
    @ApiResponse(responseCode = "200", description = "Relación creada exitosamente")
    @PostMapping
    public ResponseEntity<MisionXViaje> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva relación",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MisionXViaje.class)))
            @RequestBody MisionXViaje misionXViaje) {
        return ResponseEntity.ok(service.save(misionXViaje));
    }

    @Operation(summary = "Actualizar una relación existente Misión x Viaje")
    @ApiResponse(responseCode = "200", description = "Relación actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<MisionXViaje> update(
            @Parameter(description = "ID de la relación a actualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la relación",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MisionXViaje.class)))
            @RequestBody MisionXViaje misionXViaje) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        misionXViaje.setId(id);
        return ResponseEntity.ok(service.save(misionXViaje));
    }

    @Operation(summary = "Eliminar una relación Misión x Viaje por ID")
    @ApiResponse(responseCode = "204", description = "Relación eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Relación no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la relación a eliminar") @PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener misiones por ID de viaje", description = "Devuelve una lista de misiones asociadas a un viaje")
    @ApiResponse(responseCode = "200", description = "Misiones encontradas")
    @ApiResponse(responseCode = "404", description = "No se encontraron misiones para el viaje")
    @GetMapping("/viaje/{idViaje}/misiones")
    public ResponseEntity<List<MisionNumeroDTO>> getMisionesByViaje(
            @Parameter(description = "ID del viaje") @PathVariable Integer idViaje) {
        List<MisionNumeroDTO> misiones = service.findMisionNumeroDTOsByViajeId(idViaje);
        return misiones.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(misiones);
    }
}
