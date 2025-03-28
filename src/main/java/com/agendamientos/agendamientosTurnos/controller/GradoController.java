package com.agendamientos.agendamientosTurnos.controller;


import com.agendamientos.agendamientosTurnos.entity.Grado;
import com.agendamientos.agendamientosTurnos.service.GradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grados")
public class GradoController {

    @Autowired
    private GradoService gradoService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Grado>> getGrados() {
        List<Grado> grados = gradoService.getGrados();
        return new ResponseEntity<>(grados, HttpStatus.OK);
    }


}
