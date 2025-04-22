package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Departamento;
import com.agendamientos.agendamientosTurnos.repository.DepartamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentosService {

    @Autowired
    private DepartamentosRepository departamentosRepository;

    public List<Departamento> getAllDepartamentos() {
        return departamentosRepository.findAll();
    }

    public Optional<Departamento> getDepartamentosById(Integer id) {
        return departamentosRepository.findById(id);
    }

    public Departamento createDepartamentos(Departamento departamento) {
        // Aquí puedes añadir lógica de validación antes de guardar
        return departamentosRepository.save(departamento);
    }

    public Departamento updateDepartamentos(Integer id, Departamento departamentoDetails) {
        Optional<Departamento> departamentos = departamentosRepository.findById(id);
        if (departamentos.isPresent()) {
            Departamento existingDepartamento = departamentos.get();
            existingDepartamento.setDepartamento(departamentoDetails.getDepartamento());
            return departamentosRepository.save(existingDepartamento);
        }
        return null;
    }

    public boolean deleteDepartamentos(Integer id) {
        Optional<Departamento> departamentos = departamentosRepository.findById(id);
        if (departamentos.isPresent()) {
            departamentosRepository.delete(departamentos.get());
            return true;
        }
        return false;
    }
}