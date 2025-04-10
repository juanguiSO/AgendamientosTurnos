package com.agendamientos.agendamientosTurnos.service;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioCreateDTO;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO;
import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(FuncionarioService.class);
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
        dto.setRol(getRolNombre(funcionario.getIdRol()));


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

    private String getRolNombre (Integer idRol){
        if (idRol== null){
            return null;
        }
        Optional <Rol> rolOptional = rolRepository.findById(idRol);
        return  rolOptional.map(Rol::getNombre).orElse(null);
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

    @Transactional
    public Funcionario updateFuncionario(String cedula, @Valid FuncionarioCreateDTO funcionarioDTO) {
        logger.info("Iniciando la actualización del funcionario con cédula: {}", cedula);
        logger.debug("Datos del DTO recibidos: {}", funcionarioDTO);

        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByCedula(cedula);
        if (funcionarioOptional.isPresent()) {
            Funcionario existingFuncionario = funcionarioOptional.get();
            String contrasenaOriginal = existingFuncionario.getContrasena(); // Guardar la contraseña original
            Integer rolOriginalId = existingFuncionario.getIdRol(); // Guardar el rol original

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

            logger.debug("Campos del funcionario actualizados con los datos del DTO: {}", existingFuncionario);
            logger.debug("Rol original del funcionario: {}", rolOriginalId);
            logger.debug("Nuevo rol proporcionado en el DTO: {}", funcionarioDTO.getIdRol());
            logger.debug("Contraseña proporcionada en el DTO: {}", funcionarioDTO.getContrasena());

            // Lógica para la contraseña basada en el rol
            if (funcionarioDTO.getIdRol() != null) {
                logger.debug("Se proporcionó un nuevo idRol: {}", funcionarioDTO.getIdRol());
                // Si el rol que se está asignando es de administrador (idRol = 2)
                if (funcionarioDTO.getIdRol().equals(2)) {
                    logger.debug("El nuevo rol es Administrador.");
                    // Verificar si la contraseña actual es null o vacía y asignar la contraseña por defecto
                    if (existingFuncionario.getContrasena() == null || existingFuncionario.getContrasena().isEmpty()) {
                        String contrasenaPorDefecto = hashContrasena("defensoria");
                        existingFuncionario.setContrasena(contrasenaPorDefecto);
                        logger.debug("Contraseña por defecto asignada al administrador (contraseña anterior nula o vacía).");
                    } else if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        // Si se proporciona una nueva contraseña, hashearla
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        logger.debug("Nueva contraseña hasheada asignada al administrador.");
                    } else {
                        logger.debug("Se mantiene la contraseña existente del administrador.");
                    }
                } else if (funcionarioDTO.getIdRol().equals(1)) { // Si el rol que se está asignando es de usuario (idRol = 1)
                    logger.debug("El nuevo rol es Usuario. Estableciendo la contraseña a null.");
                    existingFuncionario.setContrasena(null);
                } else { // Para cualquier otro rol diferente de administrador o usuario
                    logger.debug("El nuevo rol no es Administrador ni Usuario (idRol: {}).", funcionarioDTO.getIdRol());
                    // Actualizar la contraseña solo si se proporciona una nueva
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        logger.debug("Nueva contraseña hasheada asignada para otro rol.");
                    } else {
                        logger.debug("No se proporcionó nueva contraseña, manteniendo la existente para otro rol.");
                        existingFuncionario.setContrasena(contrasenaOriginal);
                    }
                }
            } else {
                logger.debug("No se proporcionó un nuevo idRol en el DTO.");
                // Si no se proporciona un nuevo rol en el DTO, mantener la lógica original de la contraseña
                if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                    String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                    existingFuncionario.setContrasena(contrasenaHasheada);
                    logger.debug("Nueva contraseña hasheada asignada.");
                } else {
                    logger.debug("No se proporcionó nueva contraseña, manteniendo la existente.");
                    existingFuncionario.setContrasena(contrasenaOriginal);
                }
            }

            try {
                Funcionario funcionarioActualizado = funcionarioRepository.save(existingFuncionario);
                logger.info("Funcionario con cédula {} actualizado exitosamente: {}", cedula, funcionarioActualizado);
                return funcionarioActualizado;
            } catch (Exception e) {
                logger.error("Error al guardar la actualización del funcionario con cédula {}: {}", cedula, e.getMessage(), e);
                return null;
            }

        } else {
            logger.warn("No se encontró ningún funcionario con la cédula: {}", cedula);
            return null; // O lanzar una excepción indicando que el funcionario no existe
        }
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