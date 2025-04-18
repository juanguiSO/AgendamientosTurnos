package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.CasoDTO;
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

    @PostMapping
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


}
