package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.FuncionarioCreateDTO;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Roles;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import com.agendamientos.agendamientosTurnos.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private RolesRepository rolesRepository; // Inject RolesRepository

    public List<Funcionario> getAllFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Integer id) {
        return funcionarioRepository.findById(id);
    }

    @Transactional
    public Funcionario createFuncionario(FuncionarioCreateDTO funcionarioDTO) {
        // 1. Crear un nuevo objeto Funcionario
        Funcionario funcionario = new Funcionario();
        funcionario.setApellido(funcionarioDTO.getApellido());
        funcionario.setCedula(funcionarioDTO.getCedula());
        funcionario.setCorreo(funcionarioDTO.getCorreo());
        funcionario.setNombre(funcionarioDTO.getNombre());
        funcionario.setTelefono(funcionarioDTO.getTelefono());
        funcionario.setIdEspecialidad(funcionarioDTO.getIdEspecialidad());
        funcionario.setIdGrado(funcionarioDTO.getIdGrado());

        // 2. Cargar Roles desde la Base de Datos basándose en los IDs en el DTO
        Set<Roles> rolesPersistidos = new HashSet<>();
        if (funcionarioDTO.getRoleIds() != null) { // Check if there are Role IDs in the DTO
            for (Integer roleId : funcionarioDTO.getRoleIds()) {
                Optional<Roles> rolExistente = rolesRepository.findById(roleId);
                if (rolExistente.isPresent()) {
                    rolesPersistidos.add(rolExistente.get());
                } else {
                    throw new IllegalArgumentException("El rol con ID " + roleId + " no existe.");
                }
            }
        }
        funcionario.setRoles(rolesPersistidos);

        // 3. Verificar si el funcionario tiene el rol de administrador (después de cargar los roles)
        boolean esAdministrador = funcionario.getRoles().stream()
                .anyMatch(rol -> rol.getName() != null && rol.getName().equalsIgnoreCase("Administrador"));  // Ajusta "Administrador" al nombre real de tu rol

        // 4. Validar la contraseña si es administrador
        if (esAdministrador && (funcionarioDTO.getContrasena() == null || funcionarioDTO.getContrasena().isEmpty())) {
            throw new IllegalArgumentException("La contraseña es requerida para los administradores.");
        }

        // 5. Si no es administrador, asegúrate de que la contraseña esté vacía (o nula)
        if (!esAdministrador) {
            funcionario.setContrasena(null); // O "" si prefieres una cadena vacía
        } else {
            // 6. Hash de la contraseña (si es administrador y hay contraseña)
            if (funcionarioDTO.getContrasena() != null) { // Asegúrate de que la contraseña no sea nula
                String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                funcionario.setContrasena(contrasenaHasheada);
            }
        }

        return funcionarioRepository.save(funcionario);
    }


    public Funcionario updateFuncionario(Integer id, Funcionario funcionarioDetails) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isPresent()) {
            Funcionario existingFuncionario = funcionario.get();
            existingFuncionario.setApellido(funcionarioDetails.getApellido());
            existingFuncionario.setCedula(funcionarioDetails.getCedula());
            existingFuncionario.setCorreo(funcionarioDetails.getCorreo());
            existingFuncionario.setNombre(funcionarioDetails.getNombre());
            existingFuncionario.setTelefono(funcionarioDetails.getTelefono());
            existingFuncionario.setIdEspecialidad(funcionarioDetails.getIdEspecialidad());
            existingFuncionario.setIdGrado(funcionarioDetails.getIdGrado());
            //existingFuncionario.setContrasena(funcionarioDetails.getContrasena());  // No actualices la contraseña directamente aquí!
            existingFuncionario.setRoles(funcionarioDetails.getRoles());  // Actualiza los roles
            return funcionarioRepository.save(existingFuncionario);
        }
        return null;
    }

    public boolean deleteFuncionario(Integer id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isPresent()) {
            funcionarioRepository.delete(funcionario.get());
            return true;
        }
        return false;
    }

    // Método para hashear la contraseña (ejemplo usando BCrypt)
    private String hashContrasena(String contrasena) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(contrasena);
    }
}