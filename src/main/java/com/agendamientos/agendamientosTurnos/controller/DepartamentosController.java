package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Departamentos;
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
    public ResponseEntity<List<Departamentos>> getAllDepartamentos() {
        return new ResponseEntity<>(departamentosService.getAllDepartamentos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamentos> getDepartamentosById(@PathVariable Integer id) {
        Optional<Departamentos> departamentos = departamentosService.getDepartamentosById(id);
        return departamentos.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Departamentos> createDepartamentos(@RequestBody Departamentos departamentos) {
        return new ResponseEntity<>(departamentosService.createDepartamentos(departamentos), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamentos> updateDepartamentos(@PathVariable Integer id, @RequestBody Departamentos departamentosDetails) {
        Departamentos updatedDepartamentos = departamentosService.updateDepartamentos(id, departamentosDetails);
        if (updatedDepartamentos != null) {
            return new ResponseEntity<>(updatedDepartamentos, HttpStatus.OK);
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