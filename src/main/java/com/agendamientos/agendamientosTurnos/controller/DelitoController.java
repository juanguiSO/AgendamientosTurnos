package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Delito;
import com.agendamientos.agendamientosTurnos.service.DelitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delitos")
public class DelitoController {

    private final DelitoService delitoService;

    @Autowired
    public DelitoController(DelitoService delitoService) {
        this.delitoService = delitoService;
    }

    // GET para obtener todos los delitos
    @GetMapping
    public ResponseEntity<List<Delito>> getAllDelitos() {
        List<Delito> delitos = delitoService.getAllDelitos();
        return new ResponseEntity<>(delitos, HttpStatus.OK);
    }

    // GET para obtener un delito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Delito> getDelitoById(@PathVariable int id) {
        Optional<Delito> delito = delitoService.getDelitoById(id);
        return delito.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST para crear un nuevo delito
    @PostMapping
    public ResponseEntity<Delito> createDelito(@RequestBody Delito delito) {
        Delito savedDelito = delitoService.saveOrUpdateDelito(delito);
        return new ResponseEntity<>(savedDelito, HttpStatus.CREATED);
    }

    // PUT para actualizar un delito existente
    @PutMapping("/{id}")
    public ResponseEntity<Delito> updateDelito(@PathVariable int id, @RequestBody Delito delito) {
        Optional<Delito> existingDelito = delitoService.getDelitoById(id);
        if (existingDelito.isPresent()) {
            delito.setIdDelito(id); // Aseguramos que el ID del objeto coincida con el ID del path
            Delito updatedDelito = delitoService.saveOrUpdateDelito(delito);
            return new ResponseEntity<>(updatedDelito, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE para eliminar un delito por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelito(@PathVariable int id) {
        delitoService.deleteDelitoById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}