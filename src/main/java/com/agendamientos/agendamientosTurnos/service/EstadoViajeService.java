package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio de Spring
public class EstadoViajeService {

    @Autowired // Inyecta una instancia de EstadoViajeRepository
    private EstadoViajeRepository estadoViajeRepository;

    // Método para obtener todos los estados de viaje
    public List<EstadoViaje> findAll() {
        return estadoViajeRepository.findAll();
    }

    // Método para obtener un estado de viaje por su ID
    public Optional<EstadoViaje> findById(Integer id) {
        return estadoViajeRepository.findById(id);
    }

    // Método para guardar (crear o actualizar) un estado de viaje
    public EstadoViaje save(EstadoViaje estadoViaje) {
        return estadoViajeRepository.save(estadoViaje);
    }

    // Método para eliminar un estado de viaje por su ID
    public void deleteById(Integer id) {
        estadoViajeRepository.deleteById(id);
    }

    // Método para actualizar un estado de viaje (con validación)
    public EstadoViaje update(Integer id, EstadoViaje estadoViajeDetails) {
        Optional<EstadoViaje> optionalEstadoViaje = estadoViajeRepository.findById(id);
        if (optionalEstadoViaje.isPresent()) {
            EstadoViaje estadoViaje = optionalEstadoViaje.get();
            estadoViaje.setEstadoViaje(estadoViajeDetails.getEstadoViaje());

            return estadoViajeRepository.save(estadoViaje);
        } else {
            // Podrías lanzar una excepción personalizada aquí, por ejemplo:
            throw new RuntimeException("Estado de Viaje no encontrado con ID: " + id);
        }
    }


}