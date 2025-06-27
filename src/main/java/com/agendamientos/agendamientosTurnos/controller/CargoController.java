package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Cargo;
import com.agendamientos.agendamientosTurnos.service.CargoService;
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
@RequestMapping("/api/cargos")
@Tag(name = "Cargos", description = "API para la gestión de los cargos de los funcionarios")
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @Operation(
            summary = "Obtener todos los cargos",
            description = "Devuelve una lista con todos los cargos disponibles en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de cargos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cargo.class))
    )
    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoService.getAllCargos();
        return new ResponseEntity<>(cargos, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener un cargo por ID",
            description = "Busca un cargo específico por su ID"
    )
    @ApiResponse(responseCode = "200", description = "Cargo encontrado", content = @Content(schema = @Schema(implementation = Cargo.class)))
    @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(
            @Parameter(description = "ID del cargo a buscar", example = "1") @PathVariable Integer id) {
        Optional<Cargo> cargoOptional = cargoService.getCargoById(id);
        return cargoOptional
                .map(cargo -> new ResponseEntity<>(cargo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Crear un nuevo cargo",
            description = "Permite registrar un nuevo cargo en el sistema"
    )
    @ApiResponse(responseCode = "201", description = "Cargo creado exitosamente")
    @PostMapping
    public ResponseEntity<Cargo> createCargo(
            @Parameter(description = "Objeto cargo a crear", required = true)
            @RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.createCargo(cargo);
        return new ResponseEntity<>(createdCargo, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar un cargo",
            description = "Modifica la información de un cargo existente"
    )
    @ApiResponse(responseCode = "200", description = "Cargo actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Cargo no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Cargo> updateCargo(
            @Parameter(description = "ID del cargo a actualizar", example = "1") @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del cargo") @RequestBody Cargo cargoDetails) {
        Cargo updatedCargo = cargoService.updateCargo(id, cargoDetails);
        if (updatedCargo != null) {
            return new ResponseEntity<>(updatedCargo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Eliminar un cargo",
            description = "Realiza el borrado lógico del cargo"
    )
    @ApiResponse(responseCode = "204", description = "Cargo eliminado correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(
            @Parameter(description = "ID del cargo a eliminar", example = "1") @PathVariable Integer id) {
        cargoService.deleteCargo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
