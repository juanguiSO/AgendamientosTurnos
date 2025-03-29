package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Municipio;
import com.agendamientos.agendamientosTurnos.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    public Optional<Municipio> getMunicipioById(Integer id) {
        return municipioRepository.findById(id);
    }

    public Municipio createMunicipio(Municipio municipio) {
        // Aquí puedes añadir lógica de validación antes de guardar
        return municipioRepository.save(municipio);
    }

    public Municipio updateMunicipio(Integer id, Municipio municipioDetails) {
        Optional<Municipio> municipio = municipioRepository.findById(id);
        if (municipio.isPresent()) {
            Municipio existingMunicipio = municipio.get();
            existingMunicipio.setMunicipio(municipioDetails.getMunicipio());
            return municipioRepository.save(existingMunicipio);
        }
        return null;
    }

    public boolean deleteMunicipio(Integer id) {
        Optional<Municipio> municipio = municipioRepository.findById(id);
        if (municipio.isPresent()) {
            municipioRepository.delete(municipio.get());
            return true;
        }
        return false;
    }
}
