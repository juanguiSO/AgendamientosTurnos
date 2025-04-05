package com.agendamientos.agendamientosTurnos.service;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioCreateDTO;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO;
import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository; // Inyecta el repositorio

    @Autowired
    private GradoRepository gradoRepository;

    @Autowired
    private CargoRepository cargoRepository;


    @Transactional
    public Funcionario createFuncionario(@Valid FuncionarioCreateDTO funcionarioDTO) {
        // Validar la existencia del rol
        Rol rol = rolRepository.findById(funcionarioDTO.getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe"));

        Funcionario funcionario = new Funcionario();
        funcionario.setApellido(funcionarioDTO.getApellido());
        funcionario.setCedula(funcionarioDTO.getCedula());
        funcionario.setCorreo(funcionarioDTO.getCorreo());
        funcionario.setNombre(funcionarioDTO.getNombre());
        funcionario.setTelefono(funcionarioDTO.getTelefono());
        funcionario.setIdEspecialidad(funcionarioDTO.getIdEspecialidad());
        funcionario.setIdGrado(funcionarioDTO.getIdGrado());
        funcionario.setActivo(funcionarioDTO.getActivo());
        funcionario.setIdRol(funcionarioDTO.getIdRol());
        funcionario.setIdCargo(funcionarioDTO.getIdCargo());

        // Generar contraseña para administradores
        if (esAdmin(rol)) {
            String contrasenaHasheada = hashContrasena("defensoria");
            funcionario.setContrasena(contrasenaHasheada);
        } else if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
            String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
            funcionario.setContrasena(contrasenaHasheada);
        }

        return funcionarioRepository.save(funcionario);
    }


    public List<FuncionarioDTO> getAllFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    private FuncionarioDTO convertToDTO(Funcionario funcionario) {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setCedula(funcionario.getCedula());
        dto.setNombre(funcionario.getNombre());
        dto.setApellido(funcionario.getApellido());
        dto.setCorreo(funcionario.getCorreo());
        dto.setTelefono(funcionario.getTelefono());
        dto.setEspecialidad(getEspecialidadNombre(funcionario.getIdEspecialidad()));
        dto.setGrado(getGradoNombre(funcionario.getIdGrado()));
        dto.setCargo(getCargoNombre(funcionario.getIdCargo()));
        dto.setActivo(funcionario.getActivo());


        return dto;
    }

    private String getEspecialidadNombre(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            return null; // O un valor predeterminado si no hay especialidad
        }
        Optional<Especialidad> especialidadOptional = especialidadRepository.findById(idEspecialidad);
        return especialidadOptional.map(Especialidad::getEspecialidad).orElse(null); // O un valor predeterminado si no se encuentra
    }
    private String getCargoNombre(Integer idCargo){
        if (idCargo==null){
            return null;
        }
        Optional<Cargo> cargoOptional= cargoRepository.findById(idCargo);
        return cargoOptional.map(Cargo::getNombreCargo).orElse(null);
    }

    private String getGradoNombre (Integer idGrado){
        if (idGrado== null){
            return null;
        }
        Optional <Grado> gradoOptional = gradoRepository.findById(idGrado);
        return  gradoOptional.map(Grado::getGrado).orElse(null);
    }

    public Optional<Funcionario> getFuncionarioById(Integer id) {
        return funcionarioRepository.findById(id);
    }

    public boolean softDeleteFuncionario(String cedula) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByCedula(cedula);
        if (funcionarioOptional.isPresent()) {
            Funcionario funcionario = funcionarioOptional.get();
            funcionario.setActivo(0);
            funcionarioRepository.save(funcionario);
            return true;
        }
        return false;
    }

    public Funcionario updateFuncionario(Integer id, @Valid FuncionarioCreateDTO funcionarioDTO) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
        if (funcionarioOptional.isPresent()) {
            Funcionario existingFuncionario = funcionarioOptional.get();

            // Actualizar los campos con los valores del DTO
            existingFuncionario.setApellido(funcionarioDTO.getApellido());
            existingFuncionario.setCedula(funcionarioDTO.getCedula());
            existingFuncionario.setCorreo(funcionarioDTO.getCorreo());
            existingFuncionario.setNombre(funcionarioDTO.getNombre());
            existingFuncionario.setTelefono(funcionarioDTO.getTelefono());
            existingFuncionario.setIdEspecialidad(funcionarioDTO.getIdEspecialidad());
            existingFuncionario.setIdGrado(funcionarioDTO.getIdGrado());
            existingFuncionario.setActivo(funcionarioDTO.getActivo());
            existingFuncionario.setIdRol(funcionarioDTO.getIdRol());
            existingFuncionario.setIdCargo(funcionarioDTO.getIdCargo());

            // Actualizar la contraseña si se proporciona
            if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                existingFuncionario.setContrasena(contrasenaHasheada);
            }

            return funcionarioRepository.save(existingFuncionario);
        }
        return null; // O lanzar una excepción indicando que el funcionario no existe
    }

    public boolean deleteFuncionario(Integer id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isPresent()) {
            funcionarioRepository.delete(funcionario.get());
            return true;
        }
        return false;
    }

    // Método auxiliar para verificar si el rol es administrador
    private boolean esAdmin(Rol rol) {
        // Verificar si el rol es nulo y si el id_Rol es 2 (Admin)
        return rol != null && rol.getIdRol() == 2;
    }

    private String hashContrasena(String contrasena) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(contrasena);
    }
}