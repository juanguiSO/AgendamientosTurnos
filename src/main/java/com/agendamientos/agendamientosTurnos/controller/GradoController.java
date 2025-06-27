package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Grado;
import com.agendamientos.agendamientosTurnos.service.GradoService;
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
@RequestMapping("/api/grados")
@Tag(name = "Grados", description = "Operaciones relacionadas con los grados de los funcionarios")
public class GradoController {

    @Autowired
    private GradoService gradoService;

    @Operation(summary = "Obtener todos los grados", description = "Devuelve una lista con todos los grados registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de grados obtenida correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grado.class)))
    @GetMapping
    public ResponseEntity<List<Grado>> obtenerTodosLosGrados() {
        List<Grado> grados = gradoService.obtenerTodosLosGrados();
        return new ResponseEntity<>(grados, HttpStatus.OK);
    }

    @Operation(summary = "Obtener grado por ID", description = "Devuelve un grado específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Grado encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grado.class)))
    @ApiResponse(responseCode = "404", description = "Grado no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Grado> obtenerGradoPorId(
            @Parameter(description = "ID del grado", example = "1") @PathVariable Integer id) {
        Optional<Grado> grado = gradoService.obtenerGradoPorId(id);
        return grado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear nuevo grado", description = "Crea un nuevo grado especificando su código y nombre.")
    @ApiResponse(responseCode = "201", description = "Grado creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grado.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos para la creación del grado")
    @PostMapping
    public ResponseEntity<?> crearGrado(
            @Parameter(description = "Código del grado", example = "G001") @RequestParam String valorGrado,
            @Parameter(description = "Nombre del grado", example = "Grado Profesional") @RequestParam String valorNombreGrado) {
        try {
            Grado nuevoGrado = gradoService.crearGrado(valorGrado, valorNombreGrado);
            return new ResponseEntity<>(nuevoGrado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar grado", description = "Actualiza un grado existente por su ID.")
    @ApiResponse(responseCode = "200", description = "Grado actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grado.class)))
    @ApiResponse(responseCode = "404", description = "Grado no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para la actualización del grado")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarGrado(
            @Parameter(description = "ID del grado a actualizar", example = "1") @PathVariable Integer id,
            @RequestParam String valorGrado,
            @RequestParam String valorNombreGrado) {
        try {
            Grado gradoActualizado = gradoService.actualizarGrado(id, valorGrado, valorNombreGrado);
            if (gradoActualizado != null) {
                return new ResponseEntity<>(gradoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Grado no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar grado", description = "Elimina un grado por su ID.")
    @ApiResponse(responseCode = "200", description = "Grado eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Grado no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarGrado(
            @Parameter(description = "ID del grado a eliminar", example = "1") @PathVariable Integer id) {
        boolean eliminado = gradoService.eliminarGrado(id);
        if (eliminado) {
            return new ResponseEntity<>("Grado eliminado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Grado no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
