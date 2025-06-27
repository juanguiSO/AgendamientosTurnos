package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Departamento;
import com.agendamientos.agendamientosTurnos.entity.Municipio;
import com.agendamientos.agendamientosTurnos.service.DepartamentosService;
import com.agendamientos.agendamientosTurnos.service.MunicipioService;
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
@RequestMapping("/api/municipios")
@Tag(name = "Municipios", description = "Operaciones relacionadas con municipios y departamentos")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private DepartamentosService departamentosService;

    @Operation(summary = "Obtener todos los municipios")
    @ApiResponse(responseCode = "200", description = "Lista de municipios obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Municipio>> getAllMunicipios() {
        return new ResponseEntity<>(municipioService.getAllMunicipios(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener un municipio por ID")
    @ApiResponse(responseCode = "200", description = "Municipio encontrado")
    @ApiResponse(responseCode = "404", description = "Municipio no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Municipio> getMunicipioById(
            @Parameter(description = "ID del municipio") @PathVariable Integer id) {
        Optional<Municipio> municipio = municipioService.getMunicipioById(id);
        return municipio.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo municipio")
    @ApiResponse(responseCode = "201", description = "Municipio creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el municipio")
    @PostMapping
    public ResponseEntity<Municipio> createMunicipio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo municipio",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Municipio.class)))
            @RequestBody Municipio municipio) {
        Municipio createdMunicipio = municipioService.createMunicipio(municipio);
        if (createdMunicipio != null) {
            return new ResponseEntity<>(createdMunicipio, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Actualizar un municipio existente")
    @ApiResponse(responseCode = "200", description = "Municipio actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Municipio no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Municipio> updateMunicipio(
            @Parameter(description = "ID del municipio a actualizar") @PathVariable Integer id,
            @RequestBody Municipio municipioDetails) {
        Municipio updatedMunicipio = municipioService.updateMunicipio(id, municipioDetails);
        if (updatedMunicipio != null) {
            return new ResponseEntity<>(updatedMunicipio, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Eliminar un municipio por ID")
    @ApiResponse(responseCode = "204", description = "Municipio eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Municipio no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMunicipio(
            @Parameter(description = "ID del municipio a eliminar") @PathVariable Integer id) {
        if (municipioService.deleteMunicipio(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Obtener todos los departamentos")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos obtenida correctamente")
    @GetMapping("/departamentos")
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        return new ResponseEntity<>(departamentosService.getAllDepartamentos(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener municipios por ID de departamento")
    @ApiResponse(responseCode = "200", description = "Lista de municipios del departamento obtenida correctamente")
    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<Municipio>> getMunicipiosByDepartamentoId(
            @Parameter(description = "ID del departamento") @PathVariable Integer departamentoId) {
        return new ResponseEntity<>(municipioService.getMunicipiosByDepartamentoId(departamentoId), HttpStatus.OK);
    }
}
