package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.FuncionarioCreateDTO;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO; // Importa FuncionarioDTO
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.service.FuncionarioService;
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
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> getAllFuncionarios() { // Usa FuncionarioDTO
        return new ResponseEntity<>(funcionarioService.getAllFuncionarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> getFuncionarioById(@PathVariable Integer id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        return funcionario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody FuncionarioCreateDTO funcionarioDTO) {
        try {
            Funcionario createdFuncionario = funcionarioService.createFuncionario(funcionarioDTO);
            return new ResponseEntity<>(createdFuncionario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> updateFuncionario(@PathVariable Integer id, @Valid @RequestBody FuncionarioCreateDTO funcionarioDTO) {
        Funcionario updatedFuncionario = funcionarioService.updateFuncionario(id, funcionarioDTO);
        if (updatedFuncionario != null) {
            return new ResponseEntity<>(updatedFuncionario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Integer id) {
        if (funcionarioService.deleteFuncionario(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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