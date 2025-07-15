package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.CasoDTO;
import com.agendamientos.agendamientosTurnos.dto.CasoDetalleDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.service.CasoService;
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
@RequestMapping("/api/casos")
@Tag(name = "Casos", description = "API para la gestión de casos")
public class CasoController {

    private final CasoService casoService;

    @Autowired
    public CasoController(CasoService casoService) {
        this.casoService = casoService;
    }

    @Operation(summary = "Guardar un nuevo caso", description = "Crea y guarda un nuevo caso en el sistema.")
    @ApiResponse(responseCode = "201", description = "Caso creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Caso.class)))
    @PostMapping("/guardar")
    public ResponseEntity<Caso> guardarCaso(
            @Parameter(description = "Datos del nuevo caso") @RequestBody CasoDTO casoDTO) {
        Caso nuevoCaso = casoService.guardarCaso(casoDTO);
        return new ResponseEntity<>(nuevoCaso, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un caso por ID", description = "Elimina un caso del sistema por su ID.")
    @ApiResponse(responseCode = "204", description = "Caso eliminado exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCaso(
            @Parameter(description = "ID del caso a eliminar", example = "1") @PathVariable Integer id) {
        casoService.eliminarCaso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obtener todos los casos con nombres de relaciones", description = "Lista todos los casos con nombres de municipio, departamento, etc.")
    @ApiResponse(responseCode = "200", description = "Lista de casos obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CasoDTO.class)))
    @GetMapping("/todos")
    public ResponseEntity<List<CasoDTO>> obtenerTodosLosCasosConNombres() {
        List<CasoDTO> casosInfo = casoService.obtenerTodosLosCasosConNombres();
        return new ResponseEntity<>(casosInfo, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar un caso por ID", description = "Modifica los datos de un caso existente.")
    @ApiResponse(responseCode = "200", description = "Caso actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Caso no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PutMapping("/{id}")
    public ResponseEntity<CasoDTO> actualizarCaso(
            @Parameter(description = "ID del caso a actualizar", example = "1") @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del caso") @RequestBody CasoDTO casoDTO) {
        try {
            Optional<CasoDTO> casoActualizadoOptional = casoService.actualizarCaso(id, casoDTO);
            return casoActualizadoOptional
                    .map(casoActualizado -> new ResponseEntity<>(casoActualizado, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Obtener todos los casos con detalle", description = "Lista todos los casos incluyendo nombres de municipio y departamento.")
    @ApiResponse(responseCode = "200", description = "Casos detallados obtenidos exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CasoDetalleDTO.class)))
    @GetMapping("/detalle")
    public ResponseEntity<List<CasoDetalleDTO>> obtenerTodosLosCasosConDetalle() {
        List<CasoDetalleDTO> casosDetalle = casoService.obtenerTodosLosCasosConDetalle();
        return new ResponseEntity<>(casosDetalle, HttpStatus.OK);
    }
    @GetMapping("/detalle/activos")
    public ResponseEntity<List<CasoDetalleDTO>> obtenerCasosActivosConDetalle() {
        List<CasoDetalleDTO> casos = casoService.obtenerCasosActivosConDetalle();
        return new ResponseEntity<>(casos, HttpStatus.OK);
    }

    @GetMapping("/detalle/inactivos")
    public ResponseEntity<List<CasoDetalleDTO>> obtenerCasosInactivosConDetalle() {
        List<CasoDetalleDTO> casos = casoService.obtenerCasosInactivosConDetalle();
        return new ResponseEntity<>(casos, HttpStatus.OK);
    }


}
