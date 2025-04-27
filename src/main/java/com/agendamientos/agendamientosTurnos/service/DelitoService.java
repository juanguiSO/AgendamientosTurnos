package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Delito;
import com.agendamientos.agendamientosTurnos.repository.DelitoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DelitoService {

    private final DelitoRepository delitoRepository;

    public DelitoService(DelitoRepository delitoRepository) {
        this.delitoRepository = delitoRepository;
    }

    // Obtener todos los delitos
    public List<Delito> getAllDelitos() {
        return delitoRepository.findAll();
    }

    // Obtener un delito por ID
    public Optional<Delito> getDelitoById(int id) {
        return delitoRepository.findById(id);
    }

    // Crear o actualizar un delito
    public Delito saveOrUpdateDelito(Delito delito) {
        return delitoRepository.save(delito);
    }

    // Eliminar un delito por ID
    public void deleteDelitoById(int id) {
        delitoRepository.deleteById(id);
    }
}