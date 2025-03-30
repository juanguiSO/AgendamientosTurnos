package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Cargo;
import com.agendamientos.agendamientosTurnos.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cargos") //Ruta base para todos los endpoints de este controller
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public ResponseEntity<List<Cargo>> getAllCargos() {
        List<Cargo> cargos = cargoService.getAllCargos();
        return new ResponseEntity<>(cargos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> getCargoById(@PathVariable Integer id) {
        Optional<Cargo> cargoOptional = cargoService.getCargoById(id);
        if (cargoOptional.isPresent()) {
            return new ResponseEntity<>(cargoOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Cargo no encontrado
        }
    }

    @PostMapping
    public ResponseEntity<Cargo> createCargo(@RequestBody Cargo cargo) {
        Cargo createdCargo = cargoService.createCargo(cargo);
        return new ResponseEntity<>(createdCargo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> updateCargo(@PathVariable Integer id, @RequestBody Cargo cargoDetails) {
        Cargo updatedCargo = cargoService.updateCargo(id, cargoDetails);
        if (updatedCargo != null) {
            return new ResponseEntity<>(updatedCargo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Cargo no encontrado
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCargo(@PathVariable Integer id) {
        cargoService.deleteCargo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}