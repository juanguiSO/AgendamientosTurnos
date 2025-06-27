package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Departamento;
import com.agendamientos.agendamientosTurnos.service.DepartamentosService;
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
@RequestMapping("/api/departamentos")
@Tag(name = "Departamentos", description = "API para la gestión de departamentos")
public class DepartamentosController {

    @Autowired
    private DepartamentosService departamentosService;

    @Operation(summary = "Obtener todos los departamentos", description = "Retorna una lista con todos los departamentos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        return new ResponseEntity<>(departamentosService.getAllDepartamentos(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener un departamento por ID", description = "Devuelve un departamento específico dado su ID.")
    @ApiResponse(responseCode = "200", description = "Departamento encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentosById(
            @Parameter(description = "ID del departamento", example = "1") @PathVariable Integer id) {
        Optional<Departamento> departamentos = departamentosService.getDepartamentosById(id);
        return departamentos.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo departamento", description = "Registra un nuevo departamento en el sistema.")
    @ApiResponse(responseCode = "201", description = "Departamento creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @PostMapping
    public ResponseEntity<Departamento> createDepartamentos(
            @Parameter(description = "Datos del nuevo departamento") @RequestBody Departamento departamento) {
        return new ResponseEntity<>(departamentosService.createDepartamentos(departamento), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un departamento", description = "Actualiza los datos de un departamento existente.")
    @ApiResponse(responseCode = "200", description = "Departamento actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamentos(
            @Parameter(description = "ID del departamento a actualizar", example = "1") @PathVariable Integer id,
            @Parameter(description = "Nuevos datos del departamento") @RequestBody Departamento departamentoDetails) {
        Departamento updatedDepartamento = departamentosService.updateDepartamentos(id, departamentoDetails);
        if (updatedDepartamento != null) {
            return new ResponseEntity<>(updatedDepartamento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Eliminar un departamento", description = "Elimina un departamento por su ID.")
    @ApiResponse(responseCode = "204", description = "Departamento eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamentos(
            @Parameter(description = "ID del departamento a eliminar", example = "1") @PathVariable Integer id) {
        if (departamentosService.deleteDepartamentos(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
