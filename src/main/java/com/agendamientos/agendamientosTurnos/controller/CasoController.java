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
    @PutMapping("/{id}")  // <-- Este es el endpoint que necesitas
    public ResponseEntity<CasoDTO> actualizarCaso(@PathVariable Integer id, @RequestBody CasoDTO casoDTO) {
        try {
            Optional<CasoDTO> casoActualizadoOptional = casoService.actualizarCaso(id, casoDTO);
            return casoActualizadoOptional.map(casoActualizado -> new ResponseEntity<>(casoActualizado, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Código 400 para errores de validación
        }
    }

    // Método para obtener todos los casos con detalle (nombres de departamento y municipio)
    @GetMapping("/detalle")
    public ResponseEntity<List<CasoDetalleDTO>> obtenerTodosLosCasosConDetalle() {
        List<CasoDetalleDTO> casosDetalle = casoService.obtenerTodosLosCasosConDetalle();
        return new ResponseEntity<>(casosDetalle, HttpStatus.OK);
    }

}
