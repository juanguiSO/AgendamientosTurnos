package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import com.agendamientos.agendamientosTurnos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "Operaciones relacionadas con el manejo de vehículos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @Operation(summary = "Obtener todos los vehículos")
    @GetMapping
    public ResponseEntity<List<Vehiculo>> obtenerTodosLosVehiculos() {
        return new ResponseEntity<>(vehiculoService.obtenerTodosLosVehiculos(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener un vehículo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorId(@PathVariable Integer id) {
        Optional<Vehiculo> vehiculoOptional = vehiculoService.obtenerVehiculoPorId(id);
        return vehiculoOptional.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Guardar un nuevo vehículo")
    @PostMapping
    public ResponseEntity<Vehiculo> guardarVehiculo(@RequestBody Vehiculo vehiculo) {
        return new ResponseEntity<>(vehiculoService.guardarVehiculo(vehiculo), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar vehículo por ID (eliminación lógica o real)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Integer id) {
        vehiculoService.eliminarVehiculo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Eliminar vehículo por placa (eliminación lógica)")
    @DeleteMapping("/placa/{placa}")
    public ResponseEntity<String> eliminarVehiculoPorPlaca(@PathVariable String placa) {
        try {
            vehiculoService.eliminarVehiculoPorPlaca(placa);
            return ResponseEntity.ok("Vehículo con placa " + placa + " eliminado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el vehículo.");
        }
    }

    @Operation(summary = "Buscar vehículos por marca")
    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Vehiculo>> buscarVehiculosPorMarca(@PathVariable String marca) {
        return ResponseEntity.ok(vehiculoService.buscarVehiculosPorMarca(marca));
    }

    @Operation(summary = "Buscar vehículos por modelo y placa")
    @GetMapping("/buscar")
    public ResponseEntity<List<Vehiculo>> buscarVehiculosPorModeloYPlaca(
            @RequestParam String modelo,
            @RequestParam String placa) {
        return ResponseEntity.ok(vehiculoService.buscarVehiculosPorModeloYPlaca(modelo, placa));
    }

    @Operation(summary = "Obtener vehículos disponibles por nombre de estado")
    @GetMapping("/disponibles/nombre/{estado}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponiblesPorNombreEstado(@PathVariable String estado) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosDisponiblesPorNombreEstado(estado));
    }

    @Operation(summary = "Obtener vehículos disponibles por ID de estado")
    @GetMapping("/disponibles/id/{idEstado}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponiblesPorIdEstado(@PathVariable Integer idEstado) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosDisponiblesPorIdEstado(idEstado));
    }

    @Operation(summary = "Obtener todos los vehículos disponibles")
    @GetMapping("/disponibles")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponibles() {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosDisponibles());
    }

    @Operation(summary = "Actualizar vehículo")
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable Integer id,
                                                       @RequestBody Vehiculo vehiculoActualizado) {
        Vehiculo vehiculoActualizadoResult = vehiculoService.actualizarVehiculo(id, vehiculoActualizado);
        return vehiculoActualizadoResult != null ?
                ResponseEntity.ok(vehiculoActualizadoResult) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
