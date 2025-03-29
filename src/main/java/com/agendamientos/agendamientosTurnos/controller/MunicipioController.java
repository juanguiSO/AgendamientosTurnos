package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Municipio;
import com.agendamientos.agendamientosTurnos.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping
    public ResponseEntity<List<Municipio>> getAllMunicipios() {
        return new ResponseEntity<>(municipioService.getAllMunicipios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Municipio> getMunicipioById(@PathVariable Integer id) {
        Optional<Municipio> municipio = municipioService.getMunicipioById(id);
        return municipio.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) {
        return new ResponseEntity<>(municipioService.createMunicipio(municipio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Municipio> updateMunicipio(@PathVariable Integer id, @RequestBody Municipio municipioDetails) {
        Municipio updatedMunicipio = municipioService.updateMunicipio(id, municipioDetails);
        if (updatedMunicipio != null) {
            return new ResponseEntity<>(updatedMunicipio, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Integer id) {
        if (municipioService.deleteMunicipio(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}