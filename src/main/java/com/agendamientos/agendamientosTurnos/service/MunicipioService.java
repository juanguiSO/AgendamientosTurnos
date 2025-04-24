package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Municipio;
import com.agendamientos.agendamientosTurnos.repository.DepartamentosRepository;
import com.agendamientos.agendamientosTurnos.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private DepartamentosRepository departamentosRepository; // Necesitas el repositorio de Departamentos

    public List<Municipio> getAllMunicipios() {
        return municipioRepository.findAll();
    }

    public Optional<Municipio> getMunicipioById(Integer id) {
        return municipioRepository.findById(id);
    }

    public Municipio createMunicipio(Municipio municipio) {
        // Aquí podrías añadir lógica para validar que el idDepartamento existe
        if (municipio.getIdDepartamento() != null && departamentosRepository.existsById(municipio.getIdDepartamento())) {
            return municipioRepository.save(municipio);
        }
        // Manejar el caso en que el idDepartamento no es válido (lanzar excepción, etc.)
        return null; // O lanza una excepción
    }

    public Municipio updateMunicipio(Integer id, Municipio municipioDetails) {
        Optional<Municipio> municipioOptional = municipioRepository.findById(id);
        if (municipioOptional.isPresent()) {
            Municipio existingMunicipio = municipioOptional.get();
            existingMunicipio.setMunicipio(municipioDetails.getMunicipio());
            existingMunicipio.setIdDepartamento(municipioDetails.getIdDepartamento()); // Asegúrate de setear el idDepartamento

            // Opcional: Puedes cargar el objeto Departamento si lo necesitas en la entidad Municipio
            departamentosRepository.findById(municipioDetails.getIdDepartamento()).ifPresent(existingMunicipio::setDepartamento);

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

    // Método para obtener municipios por ID de departamento
    public List<Municipio> getMunicipiosByDepartamentoId(Integer departamentoId) {
        return municipioRepository.findByIdDepartamento(departamentoId);
    }
}