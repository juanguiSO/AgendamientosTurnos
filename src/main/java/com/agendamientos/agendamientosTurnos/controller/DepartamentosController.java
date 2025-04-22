package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Departamento;
import com.agendamientos.agendamientosTurnos.service.DepartamentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentosController {

    @Autowired
    private DepartamentosService departamentosService;

    @GetMapping
    public ResponseEntity<List<Departamento>> getAllDepartamentos() {
        return new ResponseEntity<>(departamentosService.getAllDepartamentos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamento> getDepartamentosById(@PathVariable Integer id) {
        Optional<Departamento> departamentos = departamentosService.getDepartamentosById(id);
        return departamentos.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Departamento> createDepartamentos(@RequestBody Departamento departamento) {
        return new ResponseEntity<>(departamentosService.createDepartamentos(departamento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> updateDepartamentos(@PathVariable Integer id, @RequestBody Departamento departamentoDetails) {
        Departamento updatedDepartamento = departamentosService.updateDepartamentos(id, departamentoDetails);
        if (updatedDepartamento != null) {
            return new ResponseEntity<>(updatedDepartamento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamentos(@PathVariable Integer id) {
        if (departamentosService.deleteDepartamentos(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}