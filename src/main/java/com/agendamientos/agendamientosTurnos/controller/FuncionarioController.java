package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.FuncionarioCreateDTO;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Funcionarios", description = "API para la gestión de los funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Operation(summary = "Obtener todos los funcionarios", description = "Devuelve una lista de todos los funcionarios disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de funcionarios obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioDTO.class)))
    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> getAllFuncionarios() {
        return new ResponseEntity<>(funcionarioService.getAllFuncionarios(), HttpStatus.OK);
    }

    @Operation(summary = "Buscar funcionario por ID", description = "Devuelve un funcionario según su ID si existe.")
    @ApiResponse(responseCode = "200", description = "Funcionario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class)))
    @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Integer id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        return funcionario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear nuevo funcionario", description = "Registra un nuevo funcionario en el sistema.")
    @ApiResponse(responseCode = "201", description = "Funcionario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody FuncionarioCreateDTO funcionarioDTO) {
        try {
            Funcionario createdFuncionario = funcionarioService.createFuncionario(funcionarioDTO);
            return new ResponseEntity<>(createdFuncionario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar funcionario por cédula", description = "Actualiza los datos de un funcionario existente usando su cédula.")
    @ApiResponse(responseCode = "200", description = "Funcionario actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class)))
    @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    @PutMapping("/{cedula}")
    public ResponseEntity<Funcionario> updateFuncionario(@PathVariable String cedula, @Valid @RequestBody FuncionarioCreateDTO funcionarioDTO) {
        Funcionario updatedFuncionario = funcionarioService.updateFuncionario(cedula, funcionarioDTO);
        if (updatedFuncionario != null) {
            return new ResponseEntity<>(updatedFuncionario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Eliminar funcionario por ID", description = "Elimina físicamente un funcionario según su ID.")
    @ApiResponse(responseCode = "204", description = "Funcionario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Integer id) {
        if (funcionarioService.deleteFuncionario(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Desactivar funcionario por cédula", description = "Elimina lógicamente un funcionario según su cédula.")
    @ApiResponse(responseCode = "200", description = "Funcionario desactivado correctamente")
    @ApiResponse(responseCode = "404", description = "Funcionario no encontrado")
    @DeleteMapping("/cedula/{cedula}")
    public ResponseEntity<String> deleteFuncionario(@PathVariable String cedula) {
        boolean eliminado = funcionarioService.softDeleteFuncionario(cedula);
        if (eliminado) {
            return new ResponseEntity<>("Funcionario desactivado correctamente.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Funcionario no encontrado.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
