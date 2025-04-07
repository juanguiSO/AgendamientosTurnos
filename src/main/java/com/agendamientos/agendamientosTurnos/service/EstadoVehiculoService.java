package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoVehiculo;
import com.agendamientos.agendamientosTurnos.repository.EstadoVehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoVehiculoService {

    @Autowired
    private EstadoVehiculoRepository estadoVehiculoRepository;

    public List<EstadoVehiculo> getAllEstadoVehiculos() {
        return estadoVehiculoRepository.findAll();
    }

    public Optional<EstadoVehiculo> getEstadoVehiculoById(Integer id) {
        return estadoVehiculoRepository.findById(id);
    }

    public EstadoVehiculo createEstadoVehiculo(EstadoVehiculo estadoVehiculo) {
        return estadoVehiculoRepository.save(estadoVehiculo);
    }

    public EstadoVehiculo updateEstadoVehiculo(Integer id, EstadoVehiculo estadoVehiculo) {
        Optional<EstadoVehiculo> existingEstadoVehiculo = estadoVehiculoRepository.findById(id);
        if (existingEstadoVehiculo.isPresent()) {
            estadoVehiculo.setIdEstadoVehiculo(id); // Asegurar que el ID sea el correcto
            return estadoVehiculoRepository.save(estadoVehiculo);
        }
        return null; // O lanzar una excepción indicando que no se encontró
    }

    public boolean deleteEstadoVehiculo(Integer id) {
        Optional<EstadoVehiculo> estadoVehiculo = estadoVehiculoRepository.findById(id);
        if (estadoVehiculo.isPresent()) {
            estadoVehiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}