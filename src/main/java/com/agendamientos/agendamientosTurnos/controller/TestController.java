package com.agendamientos.agendamientosTurnos.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicAccess() {
        return "Contenido público accesible para todos.";
    }

    @GetMapping("/funcionario")
    public String funcionarioAccess() {
        return "Contenido exclusivo para funcionarios.";
    }
}