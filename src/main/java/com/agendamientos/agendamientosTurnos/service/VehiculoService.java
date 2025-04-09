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

    public Vehiculo actualizarVehiculo(Integer id, Vehiculo vehiculoActualizado) {
        Optional<Vehiculo> vehiculoExistenteOptional = vehiculoRepository.findById(id);
        if (vehiculoExistenteOptional.isPresent()) {
            Vehiculo vehiculoExistente = vehiculoExistenteOptional.get();

            // Actualizar los campos que se deseen permitir modificar
            vehiculoExistente.setMarca(vehiculoActualizado.getMarca());
            vehiculoExistente.setModelo(vehiculoActualizado.getModelo());
            vehiculoExistente.setNumeroAsiento(vehiculoActualizado.getNumeroAsiento());
            vehiculoExistente.setPlaca(vehiculoActualizado.getPlaca());

            // Manejar la actualización del estado del vehículo si se proporciona un nuevo estado
            if (vehiculoActualizado.getEstadoVehiculo() != null && vehiculoActualizado.getEstadoVehiculo().getIdEstadoVehiculo() != null) {
                Optional<EstadoVehiculo> nuevoEstadoOptional = estadoVehiculoRepository.findById(vehiculoActualizado.getEstadoVehiculo().getIdEstadoVehiculo());
                nuevoEstadoOptional.ifPresent(vehiculoExistente::setEstadoVehiculo);
                // Si el nuevo estado no existe, podrías optar por lanzar una excepción o ignorar la actualización del estado.
            }

            vehiculoExistente.setActivo(vehiculoActualizado.isActivo()); // Permite actualizar el estado activo

            return vehiculoRepository.save(vehiculoExistente);
        } else {
            // Opcionalmente, puedes lanzar una excepción si el vehículo con el ID dado no existe
            return null;
        }
    }
}