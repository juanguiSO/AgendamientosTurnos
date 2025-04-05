package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Especialidad;
import com.agendamientos.agendamientosTurnos.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> obtenerTodasLasEspecialidades() { // Cambié "getAllEspecialidades"
        return especialidadRepository.findByActivoTrue();  // Recupera solo las especialidades activas (cambié el nombre)
    }

    public Optional<Especialidad> obtenerEspecialidadPorId(Integer id) { // Cambié "getEspecialidadById"
        return especialidadRepository.findById(id); // Considera verificar "activo" aquí también
    }

    public Especialidad crearEspecialidad(String valorEspecialidad) { // Cambié "createEspecialidad"

        //Validación
        if (valorEspecialidad == null || valorEspecialidad.isEmpty()){
            throw new IllegalArgumentException("Especialidad no debe estar vacía.");
        }
        if (valorEspecialidad.length() > 45){
            throw new IllegalArgumentException("Especialidad no debe tener más de 45 caracteres.");
        }

        Especialidad especialidad = new Especialidad(valorEspecialidad);
        return especialidadRepository.save(especialidad);
    }

    public Especialidad actualizarEspecialidad(Integer id, String valorEspecialidad) { // Cambié "updateEspecialidad"
        //Validación
        if (valorEspecialidad == null || valorEspecialidad.isEmpty()){
            throw new IllegalArgumentException("Especialidad no debe estar vacía.");
        }
        if (valorEspecialidad.length() > 45){
            throw new IllegalArgumentException("Especialidad no debe tener más de 45 caracteres.");
        }

        Optional<Especialidad> especialidadExistente = especialidadRepository.findById(id);
        if (especialidadExistente.isPresent()) {
            Especialidad especialidadParaActualizar = especialidadExistente.get();
            especialidadParaActualizar.setEspecialidad(valorEspecialidad);
            return especialidadRepository.save(especialidadParaActualizar);
        } else {
            return null; // O lanza una excepción indicando que la Especialidad no fue encontrada
        }
    }

    public boolean eliminarEspecialidad(Integer id) { // Cambié "deleteEspecialidad"
        Optional<Especialidad> especialidadExistente = especialidadRepository.findById(id);
        if (especialidadExistente.isPresent()) {
            Especialidad especialidadParaEliminar = especialidadExistente.get();
            especialidadParaEliminar.setActivo(false); // Eliminación lógica
            especialidadRepository.save(especialidadParaEliminar);
            return true;
        } else {
            return false; // O lanza una excepción indicando que la Especialidad no fue encontrada
        }
    }
}
