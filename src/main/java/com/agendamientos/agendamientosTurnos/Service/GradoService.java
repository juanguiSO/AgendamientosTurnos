package com.agendamientos.agendamientosTurnos.Service;


import com.agendamientos.agendamientosTurnos.Entity.Grado;
import com.agendamientos.agendamientosTurnos.Repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradoService {

    @Autowired
    GradoRepository gradoRepository;


    // Obtener un grado por su ID
    public Optional<Grado> getGrado(Long id) {
        return gradoRepository.findById(id);
    }

    // Obtener todos los Grados
    public List<Grado> getGrados() {
        return gradoRepository.findAll();
    }

    // Guardar un nuevo grado
    public Grado saveGrado(Grado grado) {
        return gradoRepository.save(grado);
    }


    // Eliminar un usuario por su ID
    public void deleteGrado(Long id) {
        Optional<Grado> gradoExistente = gradoRepository.findById(id);

        if (gradoExistente.isPresent()) {
            gradoRepository.deleteById(id); // Elimina el registro de la base de datos
        } else {
            throw new RuntimeException("Grado con ID " + id + " no encontrado.");
        }
    }


}
