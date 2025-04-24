package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.CasoDTO;
import com.agendamientos.agendamientosTurnos.dto.CasoDetalleDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.service.CasoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/casos")
public class CasoController {

    private final CasoService casoService;

    @Autowired
    public CasoController(CasoService casoService) {
        this.casoService = casoService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<Caso> guardarCaso(@RequestBody CasoDTO casoDTO) {
        Caso nuevoCaso = casoService.guardarCaso(casoDTO);
        return new ResponseEntity<>(nuevoCaso, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCaso(@PathVariable Integer id) {
        casoService.eliminarCaso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<CasoDTO>> obtenerTodosLosCasosConNombres() {
        List<CasoDTO> casosInfo = casoService.obtenerTodosLosCasosConNombres();
        return new ResponseEntity<>(casosInfo, HttpStatus.OK);
    }
    @PostMapping("/{id}")  // <-- Este es el endpoint que necesitas
    public ResponseEntity<CasoDTO> actualizarCaso(
            @PathVariable Integer id,  // <-- Obtiene el ID del caso de la URL
            @RequestBody CasoDTO casoDTO) { // <-- Obtiene los datos del caso del cuerpo de la petición (JSON)

        Optional<CasoDTO> casoActualizado = casoService.actualizarCaso(id, casoDTO);

        if (casoActualizado.isPresent()) {
            return new ResponseEntity<>(casoActualizado.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 si el caso no existe
        }
    }

    // Método para obtener todos los casos con detalle (nombres de departamento y municipio)
    @GetMapping("/detalle")
    public ResponseEntity<List<CasoDetalleDTO>> obtenerTodosLosCasosConDetalle() {
        List<CasoDetalleDTO> casosDetalle = casoService.obtenerTodosLosCasosConDetalle();
        return new ResponseEntity<>(casosDetalle, HttpStatus.OK);
    }
/**
    @GetMapping("/detalle/{id}")
    public ResponseEntity<CasoDetalleDTO> obtenerCasoConDetallePorId(@PathVariable Integer id) {
        CasoDetalleDTO casoDetalle = casoService.obtenerCasoConDetallePorId(id);
        if (casoDetalle != null) {
            return new ResponseEntity<>(casoDetalle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

}
