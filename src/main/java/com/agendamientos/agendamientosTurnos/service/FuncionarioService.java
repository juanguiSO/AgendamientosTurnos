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
        funcionario.setEsContrasenaPorDefecto(false); // Inicializamos en false

        // Generar contraseña para administradores
        if (esAdmin(rol)) {
            String contrasenaHasheada = hashContrasena("defensoria");
            funcionario.setContrasena(contrasenaHasheada);
            funcionario.setEsContrasenaPorDefecto(true); // Marcamos para actualización
        } else if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
            String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
            funcionario.setContrasena(contrasenaHasheada);
        } else {
            // Si no es admin y no se proporciona contraseña, también podríamos forzar el cambio
            String contrasenaPorDefecto = hashContrasena("temporal"); // O alguna otra lógica
            funcionario.setContrasena(contrasenaPorDefecto);
            funcionario.setEsContrasenaPorDefecto(true);
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

    public Optional<Funcionario> getFuncionarioByCedula(String cedula){
        return funcionarioRepository.findByCedula(cedula);
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

            // Lógica para la contraseña basada en el rol y actualización de esContrasenaPorDefecto
            if (funcionarioDTO.getIdRol() != null) {
                logger.debug("Se proporcionó un nuevo idRol: {}", funcionarioDTO.getIdRol());
                if (funcionarioDTO.getIdRol().equals(2)) { // Administrador
                    logger.debug("El nuevo rol es Administrador.");
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña, no es por defecto
                        logger.debug("Nueva contraseña hasheada asignada al administrador.");
                    } else if (existingFuncionario.getContrasena() == null || existingFuncionario.getContrasena().isEmpty()) {
                        String contrasenaPorDefecto = hashContrasena("defensoria");
                        existingFuncionario.setContrasena(contrasenaPorDefecto);
                        existingFuncionario.setEsContrasenaPorDefecto(true); // Contraseña por defecto
                        logger.debug("Contraseña por defecto asignada al administrador (contraseña anterior nula o vacía).");
                    } else if (!contrasenaOriginal.equals(hashContrasena("defensoria"))) {
                        // Si ya tenía una contraseña no por defecto, la mantiene
                        logger.debug("Se mantiene la contraseña existente no por defecto del administrador.");
                    } else {
                        // Si tenía la contraseña por defecto y no se proporciona nueva, la mantiene como por defecto
                        logger.debug("Se mantiene la contraseña por defecto del administrador.");
                        existingFuncionario.setEsContrasenaPorDefecto(true);
                    }
                } else if (funcionarioDTO.getIdRol().equals(1)) { // Usuario
                    logger.debug("El nuevo rol es Usuario.");
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña
                        logger.debug("Nueva contraseña hasheada asignada al usuario.");
                    } else {
                        existingFuncionario.setContrasena(null); // O podrías asignar una temporal y forzar cambio
                        existingFuncionario.setEsContrasenaPorDefecto(true); // Forzar cambio en primer login
                        logger.debug("Contraseña establecida a null/temporal para usuario, requiere actualización.");
                    }
                } else { // Para cualquier otro rol
                    logger.debug("El nuevo rol no es Administrador ni Usuario (idRol: {}).", funcionarioDTO.getIdRol());
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña
                        logger.debug("Nueva contraseña hasheada asignada para otro rol.");
                    } else {
                        logger.debug("No se proporcionó nueva contraseña, manteniendo la existente para otro rol.");
                        // No cambiamos esContrasenaPorDefecto a menos que se cambie la contraseña
                    }
                }
            } else {
                logger.debug("No se proporcionó un nuevo idRol en el DTO.");
                // Si no se proporciona un nuevo rol, actualizamos la contraseña si se proporciona
                if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                    String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                    existingFuncionario.setContrasena(contrasenaHasheada);
                    existingFuncionario.setEsContrasenaPorDefecto(false); // Se actualizó la contraseña
                    logger.debug("Nueva contraseña hasheada asignada.");
                }
                // Si no se proporciona nueva contraseña, mantenemos el estado de esContrasenaPorDefecto
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
            return null;
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