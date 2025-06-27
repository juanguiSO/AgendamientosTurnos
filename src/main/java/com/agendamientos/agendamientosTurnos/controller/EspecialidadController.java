package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Especialidad;
import com.agendamientos.agendamientosTurnos.service.EspecialidadService;
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
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades", description = "API para la gestión de especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @Operation(summary = "Obtener todas las especialidades", description = "Devuelve una lista con todas las especialidades registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class)))
    @GetMapping
    public ResponseEntity<List<Especialidad>> obtenerTodasLasEspecialidades() {
        List<Especialidad> especialidades = especialidadService.obtenerTodasLasEspecialidades();
        return new ResponseEntity<>(especialidades, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una especialidad por ID", description = "Devuelve una especialidad específica por su ID.")
    @ApiResponse(responseCode = "200", description = "Especialidad encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class)))
    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtenerEspecialidadPorId(
            @Parameter(description = "ID de la especialidad", example = "1") @PathVariable Integer id) {
        Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorId(id);
        return especialidad.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear una nueva especialidad", description = "Registra una nueva especialidad usando su nombre como parámetro.")
    @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class)))
    @ApiResponse(responseCode = "400", description = "Parámetro inválido o error en la creación")
    @PostMapping
    public ResponseEntity<?> crearEspecialidad(
            @Parameter(description = "Nombre de la especialidad a crear", example = "Medicina Forense") @RequestParam String valorEspecialidad) {
        try {
            Especialidad nuevaEspecialidad = especialidadService.crearEspecialidad(valorEspecialidad);
            return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar una especialidad", description = "Actualiza el nombre de una especialidad existente.")
    @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class)))
    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    @ApiResponse(responseCode = "400", description = "Parámetro inválido")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEspecialidad(
            @Parameter(description = "ID de la especialidad", example = "1") @PathVariable Integer id,
            @Parameter(description = "Nuevo nombre de la especialidad", example = "Psicología Forense") @RequestParam String valorEspecialidad) {
        try {
            Especialidad especialidadActualizada = especialidadService.actualizarEspecialidad(id, valorEspecialidad);
            if (especialidadActualizada != null) {
                return new ResponseEntity<>(especialidadActualizada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Especialidad no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar una especialidad", description = "Elimina una especialidad específica por su ID.")
    @ApiResponse(responseCode = "200", description = "Especialidad eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEspecialidad(
            @Parameter(description = "ID de la especialidad", example = "1") @PathVariable Integer id) {
        boolean eliminado = especialidadService.eliminarEspecialidad(id);
        if (eliminado) {
            return new ResponseEntity<>("Especialidad eliminada exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Especialidad no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
