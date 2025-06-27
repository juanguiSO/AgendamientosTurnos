package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Rol;
import com.agendamientos.agendamientosTurnos.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Operaciones CRUD para roles de usuario")
public class RolController {

    @Autowired
    private RolService rolService;

    @Operation(summary = "Obtener todos los roles")
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
        List<Rol> roles = rolService.obtenerTodosLosRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un rol por ID")
    @ApiResponse(responseCode = "200", description = "Rol encontrado")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(
            @Parameter(description = "ID del rol") @PathVariable int id) {
        Optional<Rol> rol = rolService.obtenerRolPorId(id);
        return rol.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo rol")
    @ApiResponse(responseCode = "201", description = "Rol creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    @PostMapping
    public ResponseEntity<Rol> crearRol(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Rol a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Rol.class)))
            @RequestBody Rol rol) {
        try {
            Rol nuevoRol = rolService.crearRol(rol.getNombre());
            return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar un rol existente")
    @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(
            @Parameter(description = "ID del rol a actualizar") @PathVariable int id,
            @RequestBody Rol rol) {
        try {
            Rol rolActualizado = rolService.actualizarRol(id, rol.getNombre());
            if (rolActualizado != null) {
                return new ResponseEntity<>(rolActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminar un rol por ID")
    @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(
            @Parameter(description = "ID del rol a eliminar") @PathVariable int id) {
        boolean eliminado = rolService.eliminarRol(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
