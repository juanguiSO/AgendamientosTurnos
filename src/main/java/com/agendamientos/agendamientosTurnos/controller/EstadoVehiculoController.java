package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.EstadoVehiculo;
import com.agendamientos.agendamientosTurnos.service.EstadoVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estado-vehiculos")
public class EstadoVehiculoController {

    @Autowired
    private EstadoVehiculoService estadoVehiculoService;

    @GetMapping
    public ResponseEntity<List<EstadoVehiculo>> getAllEstadoVehiculos() {
        List<EstadoVehiculo> estadoVehiculos = estadoVehiculoService.getAllEstadoVehiculos();
        return new ResponseEntity<>(estadoVehiculos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoVehiculo> getEstadoVehiculoById(@PathVariable Integer id) {
        Optional<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.getEstadoVehiculoById(id);
        return estadoVehiculo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<EstadoVehiculo> createEstadoVehiculo(@RequestBody EstadoVehiculo estadoVehiculo) {
        EstadoVehiculo createdEstadoVehiculo = estadoVehiculoService.createEstadoVehiculo(estadoVehiculo);
        return new ResponseEntity<>(createdEstadoVehiculo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoVehiculo> updateEstadoVehiculo(@PathVariable Integer id, @RequestBody EstadoVehiculo estadoVehiculo) {
        EstadoVehiculo updatedEstadoVehiculo = estadoVehiculoService.updateEstadoVehiculo(id, estadoVehiculo);
        if (updatedEstadoVehiculo != null) {
            return new ResponseEntity<>(updatedEstadoVehiculo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoVehiculo(@PathVariable Integer id) {
        boolean deleted = estadoVehiculoService.deleteEstadoVehiculo(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}