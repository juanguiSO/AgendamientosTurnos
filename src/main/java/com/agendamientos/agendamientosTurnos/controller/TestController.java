package com.agendamientos.agendamientosTurnos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Endpoints de prueba para verificar acceso público y de funcionario")
public class TestController {

    @Operation(summary = "Acceso público",
            description = "Endpoint accesible por cualquier usuario sin autenticación")
    @ApiResponse(responseCode = "200", description = "Acceso público exitoso")
    @GetMapping("/public")
    public String publicAccess() {
        return "Contenido público accesible para todos.";
    }

    @Operation(summary = "Acceso para funcionarios",
            description = "Endpoint restringido a usuarios con rol de funcionario")
    @ApiResponse(responseCode = "200", description = "Acceso autorizado para funcionarios")
    @GetMapping("/funcionario")
    public String funcionarioAccess() {
        return "Contenido exclusivo para funcionarios.";
    }
}
