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

        // Lógica para la contraseña
        // Si el rol es admin O si el funcionario no proporciona una contraseña
        // se le asignará "defensoria" como contraseña por defecto.
        if (esAdmin(rol) || (funcionarioDTO.getContrasena() == null || funcionarioDTO.getContrasena().isEmpty())) {
            String contrasenaHasheada = hashContrasena("defensoria");
            funcionario.setContrasena(contrasenaHasheada);
            funcionario.setEsContrasenaPorDefecto(true); // Marcamos para actualización
        } else {
            // Si no es admin Y se proporciona una contraseña, se usa la proporcionada.
            String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
            funcionario.setContrasena(contrasenaHasheada);
            // esContrasenaPorDefecto ya es false
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
            // Guardar la contraseña original y el rol original para comparaciones
            String contrasenaOriginal = existingFuncionario.getContrasena();
            Integer rolOriginalId = existingFuncionario.getIdRol();

            // Actualizar los campos con los valores del DTO
            existingFuncionario.setApellido(funcionarioDTO.getApellido());
            existingFuncionario.setCedula(funcionarioDTO.getCedula());
            existingFuncionario.setCorreo(funcionarioDTO.getCorreo());
            existingFuncionario.setNombre(funcionarioDTO.getNombre());
            existingFuncionario.setTelefono(funcionarioDTO.getTelefono());
            existingFuncionario.setIdEspecialidad(funcionarioDTO.getIdEspecialidad());
            existingFuncionario.setIdGrado(funcionarioDTO.getIdGrado());
            existingFuncionario.setActivo(funcionarioDTO.getActivo());
            existingFuncionario.setIdCargo(funcionarioDTO.getIdCargo());

            // **IMPORTANTE**: Antes de decidir sobre la contraseña, actualiza el ID del rol en el existingFuncionario
            // para que la lógica de contraseña se base en el NUEVO rol.
            existingFuncionario.setIdRol(funcionarioDTO.getIdRol()); // Se actualiza el rol del funcionario

            logger.debug("Campos del funcionario actualizados con los datos del DTO: {}", existingFuncionario);
            logger.debug("Rol original del funcionario: {}", rolOriginalId);
            logger.debug("Nuevo rol proporcionado en el DTO (y asignado al funcionario): {}", existingFuncionario.getIdRol()); // Usamos el rol ya asignado
            logger.debug("Contraseña proporcionada en el DTO: {}", funcionarioDTO.getContrasena());

            // Lógica para la contraseña basada en el rol y actualización de esContrasenaPorDefecto
            // Usamos el rol YA ASIGNADO al existingFuncionario, que es el nuevo rol del DTO
            if (existingFuncionario.getIdRol() != null) {
                logger.debug("El rol actual del funcionario es: {}", existingFuncionario.getIdRol());

                // Caso 1: El nuevo rol es Administrador (ID 2)
                if (existingFuncionario.getIdRol().equals(2)) {
                    logger.debug("El nuevo rol es Administrador.");
                    // Si el DTO trae una contraseña, la hasheamos y la asignamos
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña, no es por defecto
                        logger.debug("Nueva contraseña hasheada asignada al administrador.");
                    } else if (contrasenaOriginal == null || contrasenaOriginal.isEmpty() || existingFuncionario.getEsContrasenaPorDefecto()) {
                        // Si el DTO no trae contraseña Y la original era nula/vacía O por defecto, asignamos "defensoria"
                        String contrasenaPorDefecto = hashContrasena("defensoria");
                        existingFuncionario.setContrasena(contrasenaPorDefecto);
                        existingFuncionario.setEsContrasenaPorDefecto(true); // Contraseña por defecto
                        logger.debug("Contraseña 'defensoria' asignada al administrador (DTO sin contraseña o anterior por defecto).");
                    } else {
                        // Si el DTO no trae contraseña Y la original no era nula/vacía NI por defecto, mantenemos la original
                        logger.debug("No se proporcionó nueva contraseña, manteniendo la existente no por defecto del administrador.");
                        // No cambiamos la contraseña ni el estado de esContrasenaPorDefecto
                    }
                }
                // Caso 2: El nuevo rol es Usuario (ID 1)
                else if (existingFuncionario.getIdRol().equals(1)) {
                    logger.debug("El nuevo rol es Usuario.");
                    // Si el DTO trae una contraseña, la hasheamos y la asignamos
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña
                        logger.debug("Nueva contraseña hasheada asignada al usuario.");
                    } else if (contrasenaOriginal == null || contrasenaOriginal.isEmpty()) {
                        // Si el DTO no trae contraseña Y la original era nula/vacía, asignamos "defensoria"
                        String contrasenaPorDefecto = hashContrasena("defensoria");
                        existingFuncionario.setContrasena(contrasenaPorDefecto);
                        existingFuncionario.setEsContrasenaPorDefecto(true); // Contraseña por defecto
                        logger.debug("Contraseña 'defensoria' asignada al usuario (DTO sin contraseña o anterior nula/vacía).");
                    } else {
                        // Si el DTO no trae contraseña Y la original EXISTÍA, mantenemos la original
                        logger.debug("No se proporcionó nueva contraseña, manteniendo la existente del usuario.");
                        // No cambiamos la contraseña ni el estado de esContrasenaPorDefecto
                    }
                }
                // Caso 3: Cualquier otro rol (no Admin ni Usuario)
                else {
                    logger.debug("El nuevo rol no es Administrador ni Usuario (idRol: {}).", existingFuncionario.getIdRol());
                    // Si el DTO trae una contraseña, la hasheamos y la asignamos
                    if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                        String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                        existingFuncionario.setContrasena(contrasenaHasheada);
                        existingFuncionario.setEsContrasenaPorDefecto(false); // Nueva contraseña
                        logger.debug("Nueva contraseña hasheada asignada para otro rol.");
                    } else {
                        // Si no se proporciona nueva contraseña, mantenemos la existente y su estado de esContrasenaPorDefecto
                        logger.debug("No se proporcionó nueva contraseña, manteniendo la existente para otro rol.");
                    }
                }
            } else {
                // Si el idRol en el DTO es nulo, no se cambia el rol, pero se procesa la contraseña si se proporciona
                logger.debug("No se proporcionó un nuevo idRol en el DTO.");
                if (funcionarioDTO.getContrasena() != null && !funcionarioDTO.getContrasena().isEmpty()) {
                    String contrasenaHasheada = hashContrasena(funcionarioDTO.getContrasena());
                    existingFuncionario.setContrasena(contrasenaHasheada);
                    existingFuncionario.setEsContrasenaPorDefecto(false); // Se actualizó la contraseña
                    logger.debug("Nueva contraseña hasheada asignada (sin cambio de rol).");
                }
                // Si no se proporciona nueva contraseña, mantenemos el estado de esContrasenaPorDefecto
            }

            try {
                Funcionario funcionarioActualizado = funcionarioRepository.save(existingFuncionario);
                logger.info("Funcionario con cédula {} actualizado exitosamente: {}", cedula, funcionarioActualizado);
                return funcionarioActualizado;
            } catch (Exception e) {
                logger.error("Error al guardar la actualización del funcionario con cédula {}: {}", cedula, e.getMessage(), e);
                // Dependiendo de tu estrategia de manejo de errores, podrías lanzar una excepción personalizada aquí
                return null;
            }

        } else {
            logger.warn("No se encontró ningún funcionario con la cédula: {}", cedula);
            return null; // O podrías lanzar una excepción NotFoundException
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