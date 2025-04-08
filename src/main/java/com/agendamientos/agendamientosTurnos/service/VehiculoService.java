package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoVehiculo;
import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import com.agendamientos.agendamientosTurnos.repository.EstadoVehiculoRepository;
import com.agendamientos.agendamientosTurnos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final EstadoVehiculoRepository estadoVehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository, EstadoVehiculoRepository estadoVehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.estadoVehiculoRepository = estadoVehiculoRepository;
    }

    public List<String> obtenerTodosLosVehiculosActivos() {
        return vehiculoRepository.findByActivoTrue().stream()
                .map(vehiculo -> String.format(
                        "Marca: %s, Modelo: %s, Asientos: %d, Placa: %s, Estado: %s, Activo: %s",
                        vehiculo.getMarca(),
                        vehiculo.getModelo(),
                        vehiculo.getNumeroAsiento(),
                        vehiculo.getPlaca(),
                        vehiculo.getEstadoVehiculo() != null ? vehiculo.getEstadoVehiculo().getEstadoVehiculo() : "Sin Estado",
                        vehiculo.isActivo()
                ))
                .collect(Collectors.toList());
    }

    // Nuevo método para obtener todos los vehículos (activos e inactivos)
    public List<String> obtenerTodosLosVehiculos() {
        return vehiculoRepository.findAll().stream()
                .map(vehiculo -> String.format(
                        "Marca: %s, Modelo: %s, Asientos: %d, Placa: %s, Estado: %s, Activo: %s",
                        vehiculo.getMarca(),
                        vehiculo.getModelo(),
                        vehiculo.getNumeroAsiento(),
                        vehiculo.getPlaca(),
                        vehiculo.getEstadoVehiculo() != null ? vehiculo.getEstadoVehiculo().getEstadoVehiculo() : "Sin Estado",
                        vehiculo.isActivo()
                ))
                .collect(Collectors.toList());
    }

    public Optional<Vehiculo> obtenerVehiculoPorId(Integer id) {
        return vehiculoRepository.findById(id);
    }

    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        vehiculo.setActivo(true);
        return vehiculoRepository.save(vehiculo);
    }

    public void eliminarVehiculo(Integer id) {
        Optional<Vehiculo> vehiculoOptional = vehiculoRepository.findById(id);
        vehiculoOptional.ifPresent(vehiculo -> {
            vehiculo.setActivo(false);
            vehiculoRepository.save(vehiculo);
        });
    }

    @Transactional
    public void eliminarVehiculoPorPlaca(String placa) {
        Optional<Vehiculo> vehiculoOptional = vehiculoRepository.findByPlaca(placa);
        if (vehiculoOptional.isPresent()) {
            vehiculoRepository.marcarComoInactivoPorPlaca(placa);
        } else {
            throw new RuntimeException("No se encontró ningún vehículo con la placa: " + placa);
        }
    }

    public List<Vehiculo> buscarVehiculosPorMarca(String marca) {
        return vehiculoRepository.findByMarcaAndActivoTrue(marca);
    }

    public List<Vehiculo> buscarVehiculosPorModeloYPlaca(String modelo, String placa) {
        return vehiculoRepository.findByModeloAndPlacaAndActivoTrue(modelo, placa);
    }

    public List<Vehiculo> obtenerVehiculosDisponiblesPorNombreEstado(String estadoDisponible) {
        return vehiculoRepository.findByEstadoVehiculo_EstadoVehiculoAndActivoTrue(estadoDisponible);
    }

    public List<Vehiculo> obtenerVehiculosDisponiblesPorIdEstado(Integer idEstadoDisponible) {
        return vehiculoRepository.findByEstadoVehiculo_IdEstadoVehiculoAndActivoTrue(idEstadoDisponible);
    }

    public List<Vehiculo> obtenerVehiculosDisponibles() {
        Optional<EstadoVehiculo> estadoDisponibleOptional = estadoVehiculoRepository.findByEstadoVehiculo("Disponible");
        if (estadoDisponibleOptional.isPresent()) {
            return vehiculoRepository.findByEstadoVehiculo_IdEstadoVehiculoAndActivoTrue(estadoDisponibleOptional.get().getIdEstadoVehiculo());
        }
        return List.of();
    }
}