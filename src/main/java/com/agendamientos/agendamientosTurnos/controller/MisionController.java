package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.CrearMisionDTO;
import com.agendamientos.agendamientosTurnos.dto.MisionDTO;
import com.agendamientos.agendamientosTurnos.dto.ReporteFuncionarioCasosDTO;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.service.MisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/misiones")
@Tag(name = "Misiones", description = "Operaciones relacionadas con las misiones asignadas a casos y funcionarios")
public class MisionController {

    private final MisionService misionService;

    @Autowired
    public MisionController(MisionService misionService) {
        this.misionService = misionService;
    }

    @Operation(summary = "Obtener misión por número", description = "Busca una misión específica por su número.")
    @ApiResponse(responseCode = "200", description = "Misión encontrada")
    @ApiResponse(responseCode = "404", description = "Misión no encontrada")
    @GetMapping("/{numeroMision}")
    public ResponseEntity<?> obtenerMisionPorNumero(@PathVariable Integer numeroMision) {
        Optional<Mision> misionOptional = misionService.obtenerMisionPorNumero(numeroMision);
        return misionOptional.map(mision -> new ResponseEntity<>(mision, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener todas las misiones con información detallada")
    @GetMapping("/todos")
    public ResponseEntity<List<MisionDTO>> obtenerTodasLasMisionesConInfo() {
        List<MisionDTO> misionesInfo = misionService.obtenerTodasLasMisionesDTO();
        return new ResponseEntity<>(misionesInfo, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todas las misiones (modo básico)")
    @GetMapping("/basico")
    public ResponseEntity<List<Mision>> obtenerTodasLasMisionesBasico() {
        List<Mision> todasLasMisiones = misionService.obtenerTodasLasMisiones();
        return new ResponseEntity<>(todasLasMisiones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar misiones por actividad")
    @GetMapping("/buscar")
    public ResponseEntity<List<Mision>> buscarMisionesPorActividad(
            @Parameter(description = "Nombre de la actividad") @RequestParam String actividad) {
        List<Mision> misiones = misionService.buscarMisionesPorActividad(actividad);
        return new ResponseEntity<>(misiones, HttpStatus.OK);
    }

    @Operation(summary = "Obtener misiones por ID de caso")
    @GetMapping("/caso/{idCaso}")
    public ResponseEntity<List<Mision>> obtenerMisionesPorCaso(@PathVariable Integer idCaso) {
        List<Mision> misiones = misionService.obtenerMisionesPorCaso(idCaso);
        return new ResponseEntity<>(misiones, HttpStatus.OK);
    }

    @Operation(summary = "Obtener misiones activas")
    @GetMapping("/activas")
    public ResponseEntity<List<MisionDTO>> obtenerMisionesActivas() {
        List<MisionDTO> misionesActivas = misionService.obtenerTodasLasMisionesActivasDTO();
        return new ResponseEntity<>(misionesActivas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener misiones inactivas")
    @GetMapping("/inactivas")
    public ResponseEntity<List<MisionDTO>> obtenerMisionesInactivas() {
        List<MisionDTO> misionesInactivas = misionService.obtenerTodasLasMisionesInactivasDTO();
        return new ResponseEntity<>(misionesInactivas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener misiones activas por caso")
    @GetMapping("/caso/{idCaso}/activas")
    public ResponseEntity<List<Mision>> obtenerMisionesActivasPorCaso(@PathVariable Integer idCaso) {
        List<Mision> misionesActivasPorCaso = misionService.obtenerMisionesActivasPorCaso(idCaso);
        return new ResponseEntity<>(misionesActivasPorCaso, HttpStatus.OK);
    }

    @Operation(summary = "Crear nueva misión")
    @ApiResponse(responseCode = "201", description = "Misión creada exitosamente")
    @PostMapping("/crear")
    public ResponseEntity<?> guardarNuevaMision(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear la misión",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CrearMisionDTO.class))
            )
            @RequestBody CrearMisionDTO crearMisionDTO) {
        return misionService.guardarMision(crearMisionDTO);
    }

    @Operation(summary = "Eliminar misión por número")
    @DeleteMapping("/{numeroMision}")
    public ResponseEntity<Void> eliminarMision(@PathVariable Integer numeroMision) {
        misionService.eliminarMision(numeroMision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Marcar misión como inactiva")
    @PutMapping("/{numeroMision}/inactiva")
    public ResponseEntity<Void> marcarMisionComoInactiva(@PathVariable Integer numeroMision) {
        misionService.marcarMisionComoInactiva(numeroMision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Actualizar misión existente")
    @PutMapping("/{numeroMision}")
    public ResponseEntity<?> actualizarMision(
            @Parameter(description = "Número de la misión a actualizar") @PathVariable Integer numeroMision,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la misión",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MisionDTO.class))
            )
            @RequestBody MisionDTO misionDTO) {
        Optional<Mision> misionActualizada = misionService.actualizarMision(numeroMision, misionDTO);
        return misionActualizada.map(mision -> new ResponseEntity<>(mision, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener reporte de funcionarios con sus casos asignados")
    @GetMapping("/reporte/funcionarios-casos")
    public ResponseEntity<List<ReporteFuncionarioCasosDTO>> obtenerReporteFuncionariosCasos() {
        List<ReporteFuncionarioCasosDTO> reporte = misionService.obtenerReporteFuncionariosConCasos();
        return ResponseEntity.ok(reporte);
    }
}