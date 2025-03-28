package com.agendamientos.agendamientosTurnos.Controller;


import com.agendamientos.agendamientosTurnos.Entity.Grado;
import com.agendamientos.agendamientosTurnos.Service.GradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/grados")
public class GradoController {

    @Autowired
    private GradoService gradoService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Grado>> getUsuarios() {
        List<Grado> usuarios = gradoService.getGrados();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }


}
