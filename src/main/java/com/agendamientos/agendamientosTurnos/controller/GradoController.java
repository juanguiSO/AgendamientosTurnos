package com.agendamientos.agendamientosTurnos.controller;


import com.agendamientos.agendamientosTurnos.entity.Grado;
import com.agendamientos.agendamientosTurnos.service.GradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grados")
public class GradoController {

    @Autowired
    private GradoService gradoService;

    @GetMapping
    public ResponseEntity<List<Grado>> obtenerTodosLosGrados() { // Cambié "getAllGrados"
        List<Grado> grados = gradoService.obtenerTodosLosGrados(); // Cambié "getAllGrados"
        return new ResponseEntity<>(grados, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grado> obtenerGradoPorId(@PathVariable Integer id) { // Cambié "getGradoById"
        Optional<Grado> grado = gradoService.obtenerGradoPorId(id); // Cambié "getGradoById"
        return grado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> crearGrado(@RequestParam String valorGrado, @RequestParam String valorNombreGrado) { // Cambié "createGrado"
        try {
            Grado nuevoGrado = gradoService.crearGrado(valorGrado, valorNombreGrado); // Cambié "createGrado"
            return new ResponseEntity<>(nuevoGrado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarGrado(@PathVariable Integer id, @RequestParam String valorGrado, @RequestParam String valorNombreGrado) { // Cambié "updateGrado"
        try {
            Grado gradoActualizado = gradoService.actualizarGrado(id, valorGrado, valorNombreGrado); // Cambié "updateGrado"
            if (gradoActualizado != null) {
                return new ResponseEntity<>(gradoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Grado no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarGrado(@PathVariable Integer id) { // Cambié "deleteGrado"
        boolean eliminado = gradoService.eliminarGrado(id); // Cambié "deleteGrado"
        if (eliminado) {
            return new ResponseEntity<>("Grado eliminado exitosamente", HttpStatus.OK); // Cambié el mensaje
        } else {
            return new ResponseEntity<>("Grado no encontrado", HttpStatus.NOT_FOUND);
        }
    }


}
