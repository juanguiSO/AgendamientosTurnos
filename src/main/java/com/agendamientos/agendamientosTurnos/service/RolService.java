package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Rol;
import com.agendamientos.agendamientosTurnos.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerRolPorId(int id) {
        return rolRepository.findById(id);
    }

    public Rol crearRol(String nombre) {
        // Validación
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no debe estar vacío.");
        }
        if (nombre.length() > 45) {
            throw new IllegalArgumentException("El nombre del rol no debe tener más de 45 caracteres.");
        }

        Rol rol = new Rol(nombre);
        return rolRepository.save(rol);
    }

    public Rol actualizarRol(int id, String nombre) {
        // Validación
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no debe estar vacío.");
        }
        if (nombre.length() > 45) {
            throw new IllegalArgumentException("El nombre del rol no debe tener más de 45 caracteres.");
        }

        Optional<Rol> rolExistente = rolRepository.findById(id);
        if (rolExistente.isPresent()) {
            Rol rolParaActualizar = rolExistente.get();
            rolParaActualizar.setNombre(nombre);
            return rolRepository.save(rolParaActualizar);
        } else {
            return null; // O lanza una excepción indicando que el Rol no fue encontrado
        }
    }
    // Método auxiliar para verificar si el rol es administrador
    private boolean esAdmin(Rol rol) {
        // Verificar si el rol es nulo y si el id_Rol es 2 (Admin)
        return rol != null && rol.getIdRol() == 2;
    }


    public boolean eliminarRol(int id) {
        Optional<Rol> rolExistente = rolRepository.findById(id);
        if (rolExistente.isPresent()) {
            rolRepository.deleteById(id); // Eliminación física
            return true;
        } else {
            return false; // O lanza una excepción indicando que el Rol no fue encontrado
        }
    }
}