package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.repository.MisionXViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MisionXViajeService {
    private final MisionXViajeRepository repository;

    public MisionXViajeService(MisionXViajeRepository repository) {
        this.repository = repository;
    }

    public List<MisionXViaje> findAll() {
        return repository.findAll();
    }

    public Optional<MisionXViaje> findById(Long id) {
        return repository.findById(id);
    }

    public MisionXViaje save(MisionXViaje misionXViaje) {
        return repository.save(misionXViaje);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<MisionXViaje> findByViajeId(Integer idViaje) {
        return repository.findByViaje_IdViaje(idViaje);
    }

}