package com.agendamientos.agendamientosTurnos.service;


import com.agendamientos.agendamientosTurnos.entity.Grado;
import com.agendamientos.agendamientosTurnos.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradoService {

    @Autowired
    GradoRepository gradoRepository;



    public List<Grado> obtenerTodosLosGrados() { // Cambié "getAllGrados"
        return gradoRepository.findByActivoTrue(); // Recupera solo los grados activos (cambié el nombre)
    }

    public Optional<Grado> obtenerGradoPorId(Long id) { // Cambié "getGradoById"
        return gradoRepository.findById(id);  // Considera verificar "activo" aquí también, si es necesario
    }

    public Grado crearGrado(String valorGrado, String valorNombreGrado) { // Cambié "createGrado"

        //Validación
        if (valorGrado == null || valorGrado.isEmpty() || valorNombreGrado == null || valorNombreGrado.isEmpty()){
            throw new IllegalArgumentException("Grado y Nombre del grado no deben estar vacíos.");
        }
        if (valorGrado.length() > 45){
            throw new IllegalArgumentException("Grado no debe tener más de 45 caracteres.");
        }

        if (valorNombreGrado.length() > 4){
            throw new IllegalArgumentException("NombreGradoValue no debe tener más de 4 caracteres.");
        }

        Grado grado = new Grado(valorGrado, valorNombreGrado);
        return gradoRepository.save(grado);
    }

    public Grado actualizarGrado(Long id, String valorGrado, String valorNombreGrado) { // Cambié "updateGrado"

        //Validación
        if (valorGrado == null || valorGrado.isEmpty() || valorNombreGrado == null || valorNombreGrado.isEmpty()){
            throw new IllegalArgumentException("Grado y Nombre del grado no deben estar vacíos.");
        }
        if (valorGrado.length() > 45){
            throw new IllegalArgumentException("Grado no debe tener más de 45 caracteres.");
        }

        if (valorNombreGrado.length() > 4){
            throw new IllegalArgumentException("NombreGradoValue no debe tener más de 4 caracteres.");
        }

        Optional<Grado> gradoExistente = gradoRepository.findById(id);
        if (gradoExistente.isPresent()) {
            Grado gradoParaActualizar = gradoExistente.get();
            gradoParaActualizar.setGrado(valorGrado);
               return gradoRepository.save(gradoParaActualizar);
        } else {
            return null; // O lanza una excepción indicando que el Grado no fue encontrado
        }
    }

    public boolean eliminarGrado(Long id) { // Cambié "deleteGrado"
        Optional<Grado> gradoExistente = gradoRepository.findById(id);
        if (gradoExistente.isPresent()) {
            Grado gradoParaEliminar = gradoExistente.get();
            gradoParaEliminar.setActivo(false);  // Eliminación lógica
            gradoRepository.save(gradoParaEliminar);
            return true;
        } else {
            return false; // O lanza una excepción indicando que el Grado no fue encontrado
        }
    }





}
