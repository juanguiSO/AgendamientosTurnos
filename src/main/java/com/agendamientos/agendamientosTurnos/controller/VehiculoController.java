package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import com.agendamientos.agendamientosTurnos.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<Vehiculo>> obtenerTodosLosVehiculos() {
        List<Vehiculo> todosVehiculosInfo = vehiculoService.obtenerTodosLosVehiculos();
        return new ResponseEntity<>(todosVehiculosInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerVehiculoPorId(@PathVariable Integer id) {
        Optional<Vehiculo> vehiculoOptional = vehiculoService.obtenerVehiculoPorId(id);
        return vehiculoOptional.map(vehiculo -> new ResponseEntity<>(vehiculo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Vehiculo> guardarVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo nuevoVehiculo = vehiculoService.guardarVehiculo(vehiculo);
        return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Integer id) {
        vehiculoService.eliminarVehiculo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/placa/{placa}")
    public ResponseEntity<String> eliminarVehiculoPorPlaca(@PathVariable String placa) {
        try {
            vehiculoService.eliminarVehiculoPorPlaca(placa);
            return new ResponseEntity<>("Vehículo con placa " + placa + " eliminado exitosamente (lógicamente).", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrió un error al eliminar el vehículo.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Vehiculo>> buscarVehiculosPorMarca(@PathVariable String marca) {
        List<Vehiculo> vehiculos = vehiculoService.buscarVehiculosPorMarca(marca);
        return new ResponseEntity<>(vehiculos, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Vehiculo>> buscarVehiculosPorModeloYPlaca(
            @RequestParam String modelo,
            @RequestParam String placa) {
        List<Vehiculo> vehiculos = vehiculoService.buscarVehiculosPorModeloYPlaca(modelo, placa);
        return new ResponseEntity<>(vehiculos, HttpStatus.OK);
    }

    @GetMapping("/disponibles/nombre/{estado}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponiblesPorNombreEstado(@PathVariable String estado) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosDisponiblesPorNombreEstado(estado);
        return new ResponseEntity<>(vehiculos, HttpStatus.OK);
    }

    @GetMapping("/disponibles/id/{idEstado}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponiblesPorIdEstado(@PathVariable Integer idEstado) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosDisponiblesPorIdEstado(idEstado);
        return new ResponseEntity<>(vehiculos, HttpStatus.OK);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosDisponibles() {
        List<Vehiculo> vehiculos = vehiculoService.obtenerVehiculosDisponibles();
        return new ResponseEntity<>(vehiculos, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable Integer id, @RequestBody Vehiculo vehiculoActualizado) {
        Vehiculo vehiculoActualizadoResult = vehiculoService.actualizarVehiculo(id, vehiculoActualizado);
        if (vehiculoActualizadoResult != null) {
            return new ResponseEntity<>(vehiculoActualizadoResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}