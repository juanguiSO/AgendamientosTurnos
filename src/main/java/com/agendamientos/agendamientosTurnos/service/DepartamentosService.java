package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Departamentos;
import com.agendamientos.agendamientosTurnos.repository.DepartamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentosService {

    @Autowired
    private DepartamentosRepository departamentosRepository;

    public List<Departamentos> getAllDepartamentos() {
        return departamentosRepository.findAll();
    }

    public Optional<Departamentos> getDepartamentosById(Integer id) {
        return departamentosRepository.findById(id);
    }

    public Departamentos createDepartamentos(Departamentos departamentos) {
        // Aquí puedes añadir lógica de validación antes de guardar
        return departamentosRepository.save(departamentos);
    }

    public Departamentos updateDepartamentos(Integer id, Departamentos departamentosDetails) {
        Optional<Departamentos> departamentos = departamentosRepository.findById(id);
        if (departamentos.isPresent()) {
            Departamentos existingDepartamentos = departamentos.get();
            existingDepartamentos.setDepartamento(departamentosDetails.getDepartamento());
            return departamentosRepository.save(existingDepartamentos);
        }
        return null;
    }

    public boolean deleteDepartamentos(Integer id) {
        Optional<Departamentos> departamentos = departamentosRepository.findById(id);
        if (departamentos.isPresent()) {
            departamentosRepository.delete(departamentos.get());
            return true;
        }
        return false;
    }
}