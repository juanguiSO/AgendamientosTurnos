package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.service.MisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/misiones")
public class MisionController {

    private final MisionService misionService;

    @Autowired
    public MisionController(MisionService misionService) {
        this.misionService = misionService;
    }

    @GetMapping("/{numeroMision}")
    public ResponseEntity<?> obtenerMisionPorNumero(@PathVariable Integer numeroMision) {
        Optional<Mision> misionOptional = misionService.obtenerMisionPorNumero(numeroMision);
        return misionOptional.map(mision -> new ResponseEntity<>(mision, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Modifica el endpoint para listar todas las misiones con la información deseada
    @GetMapping("/todos")
    public ResponseEntity<List<String>> obtenerTodasLasMisionesConInfo() {
        List<String> misionesInfo = misionService.obtenerTodasLasMisionesConInfo();
        return new ResponseEntity<>(misionesInfo, HttpStatus.OK);
    }

    // Puedes mantener el endpoint básico si lo necesitas
    @GetMapping("/basico")
    public ResponseEntity<List<Mision>> obtenerTodasLasMisionesBasico() {
        List<Mision> todasLasMisiones = misionService.obtenerTodasLasMisiones();
        return new ResponseEntity<>(todasLasMisiones, HttpStatus.OK);
    }



    @GetMapping("/buscar")
    public ResponseEntity<List<Mision>> buscarMisionesPorActividad(@RequestParam String actividad) {
        List<Mision> misiones = misionService.buscarMisionesPorActividad(actividad);
        return new ResponseEntity<>(misiones, HttpStatus.OK);
    }

    @GetMapping("/caso/{idCaso}")
    public ResponseEntity<List<Mision>> obtenerMisionesPorCaso(@PathVariable Integer idCaso) {
        List<Mision> misiones = misionService.obtenerMisionesPorCaso(idCaso);
        return new ResponseEntity<>(misiones, HttpStatus.OK);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Mision>> obtenerMisionesActivas() {
        List<Mision> misionesActivas = misionService.obtenerMisionesActivas();
        return new ResponseEntity<>(misionesActivas, HttpStatus.OK);
    }

    @GetMapping("/inactivas")
    public ResponseEntity<List<Mision>> obtenerMisionesInactivas() {
        List<Mision> misionesInactivas = misionService.obtenerMisionesInactivas();
        return new ResponseEntity<>(misionesInactivas, HttpStatus.OK);
    }

    @GetMapping("/caso/{idCaso}/activas")
    public ResponseEntity<List<Mision>> obtenerMisionesActivasPorCaso(@PathVariable Integer idCaso) {
        List<Mision> misionesActivasPorCaso = misionService.obtenerMisionesActivasPorCaso(idCaso);
        return new ResponseEntity<>(misionesActivasPorCaso, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mision> guardarMision(@RequestBody Mision mision) {
        Mision nuevaMision = misionService.guardarMision(mision);
        return new ResponseEntity<>(nuevaMision, HttpStatus.CREATED);
    }

    @DeleteMapping("/{numeroMision}")
    public ResponseEntity<Void> eliminarMision(@PathVariable Integer numeroMision) {
        misionService.eliminarMision(numeroMision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{numeroMision}/inactiva")
    public ResponseEntity<Void> marcarMisionComoInactiva(@PathVariable Integer numeroMision) {
        misionService.marcarMisionComoInactiva(numeroMision);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}