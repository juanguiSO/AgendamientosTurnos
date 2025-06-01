package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.service.MisionXViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/misionxviaje")
public class MisionXViajeController {
    @Autowired
    private MisionXViajeService service;

    @PostMapping
    public ResponseEntity<MisionXViaje> crearRelacion(@RequestBody MisionXViaje misionXViaje) {
        return ResponseEntity.ok(service.guardarRelacion(misionXViaje));
    }

    @GetMapping("/mision/{numeroMision}")
    public ResponseEntity<List<MisionXViaje>> obtenerPorMision(@PathVariable Long numeroMision) {
        return ResponseEntity.ok(service.obtenerPorMision(numeroMision));
    }

    @GetMapping("/viaje/{idViaje}")
    public ResponseEntity<List<MisionXViaje>> obtenerPorViaje(@PathVariable Long idViaje) {
        return ResponseEntity.ok(service.obtenerPorViaje(idViaje));
    }
}