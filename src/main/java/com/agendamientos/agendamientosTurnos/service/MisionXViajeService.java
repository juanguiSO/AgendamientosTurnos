package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.repository.MisionXViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MisionXViajeService {
    @Autowired
    private MisionXViajeRepository repository;

    public MisionXViaje guardarRelacion(MisionXViaje misionXViaje) {
        return repository.save(misionXViaje);
    }

    public List<MisionXViaje> obtenerPorMision(Long numeroMision) {
        return repository.findByMision_NumeroMision(numeroMision);
    }

    public List<MisionXViaje> obtenerPorViaje(Long idViaje) {
        return repository.findByViaje_IdViaje(idViaje);
    }
}