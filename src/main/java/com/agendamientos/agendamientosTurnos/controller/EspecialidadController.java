package com.agendamientos.agendamientosTurnos.controller;


import com.agendamientos.agendamientosTurnos.entity.Especialidad;
import com.agendamientos.agendamientosTurnos.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> obtenerTodasLasEspecialidades() { // Cambié "getAllEspecialidades"
        List<Especialidad> especialidades = especialidadService.obtenerTodasLasEspecialidades(); // Cambié "getAllEspecialidades"
        return new ResponseEntity<>(especialidades, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtenerEspecialidadPorId(@PathVariable Integer id) { // Cambié "getEspecialidadById"
        Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorId(id); // Cambié "getEspecialidadById"
        return especialidad.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> crearEspecialidad(@RequestParam String valorEspecialidad) { // Cambié "createEspecialidad"
        try {
            Especialidad nuevaEspecialidad = especialidadService.crearEspecialidad(valorEspecialidad); // Cambié "createEspecialidad"
            return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEspecialidad(@PathVariable Integer id, @RequestParam String valorEspecialidad) { // Cambié "updateEspecialidad"
        try {
            Especialidad especialidadActualizada = especialidadService.actualizarEspecialidad(id, valorEspecialidad); // Cambié "updateEspecialidad"
            if (especialidadActualizada != null) {
                return new ResponseEntity<>(especialidadActualizada, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Especialidad no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEspecialidad(@PathVariable Integer id) { // Cambié "deleteEspecialidad"
        boolean eliminado = especialidadService.eliminarEspecialidad(id); // Cambié "deleteEspecialidad"
        if (eliminado) {
            return new ResponseEntity<>("Especialidad eliminada exitosamente", HttpStatus.OK); // Cambié el mensaje
        } else {
            return new ResponseEntity<>("Especialidad no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}