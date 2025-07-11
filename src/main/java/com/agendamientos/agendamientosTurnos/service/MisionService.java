package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.CrearMisionDTO;
import com.agendamientos.agendamientosTurnos.dto.MisionDTO;
import com.agendamientos.agendamientosTurnos.dto.ReporteFuncionarioCasosDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Especialidad;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.repository.CasoRepository;
import com.agendamientos.agendamientosTurnos.repository.EspecialidadRepository;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MisionService {
    private static final Logger logger = LoggerFactory.getLogger(MisionService.class);


    private final MisionRepository misionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CasoRepository casoRepository;
    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public MisionService(MisionRepository misionRepository, FuncionarioRepository funcionarioRepository, CasoRepository casoRepository,EspecialidadRepository especialidadRepository) {
        this.misionRepository = misionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.casoRepository = casoRepository;
        this.especialidadRepository =especialidadRepository;
    }

    public Optional<Mision> obtenerMisionPorNumero(Integer numeroMision) {
        return misionRepository.findByNumeroMision(numeroMision);
    }


    public List<Mision> buscarMisionesPorActividad(String actividad) {
        return misionRepository.findByActividadesContaining(actividad);
    }

    public List<Mision> obtenerMisionesPorCaso(Integer idCaso) {
        return misionRepository.findByCaso_IdCaso(idCaso);
    }

    public List<Mision> obtenerMisionesActivas() {
        return misionRepository.findByActivoTrue();
    }

    public List<Mision> obtenerMisionesInactivas() {
        return misionRepository.findByActivoFalse();
    }

    public List<Mision> obtenerMisionesActivasPorCaso(Integer idCaso) {
        return misionRepository.findByCaso_IdCasoAndActivoTrue(idCaso);
    }

    public List<Mision> obtenerTodasLasMisiones() {
        return misionRepository.findAll();
    }

    public Optional<Mision> obtenerMisionPorId(Integer id) {
        return misionRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<?> guardarMision(CrearMisionDTO crearMisionDTO) {
        logger.info("Guardando nueva misión con DTO: {}", crearMisionDTO);

        if (crearMisionDTO.getNumeroMision() == null ||
                !String.valueOf(crearMisionDTO.getNumeroMision()).matches("\\d{9}")) {
            logger.warn("Número de misión inválido: {}", crearMisionDTO.getNumeroMision());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "El número de misión debe tener exactamente 9 dígitos numéricos."));
        }
        Mision nuevaMision = new Mision();
        nuevaMision.setNumeroMision(crearMisionDTO.getNumeroMision());
        nuevaMision.setActividades(crearMisionDTO.getActividades());
        nuevaMision.setActivo(crearMisionDTO.getActivo());
        logger.info("Nueva misión creada (sin relaciones aún): {}", nuevaMision);

        // Relación con Funcionario (verificar si ya está asignado al mismo caso)
        if (crearMisionDTO.getCedulaFuncionario() != null && !crearMisionDTO.getCedulaFuncionario().isEmpty()) {
            logger.info("Buscando funcionario con cédula: {}", crearMisionDTO.getCedulaFuncionario());
            Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByCedula(crearMisionDTO.getCedulaFuncionario());

            if (funcionarioOptional.isPresent()) {
                Funcionario funcionario = funcionarioOptional.get();

                // Validamos si el funcionario ya está asignado al mismo caso
                if (crearMisionDTO.getNumeroCaso() != null && !crearMisionDTO.getNumeroCaso().isEmpty()) {
                    Optional<Mision> misionExistente = misionRepository.findByCasoCodigoCasoAndFuncionarioCedula(
                            crearMisionDTO.getNumeroCaso(), crearMisionDTO.getCedulaFuncionario());

                    if (misionExistente.isPresent()) {
                        logger.warn("El funcionario con cédula {} ya está asignado al caso {}",
                                crearMisionDTO.getCedulaFuncionario(), crearMisionDTO.getNumeroCaso());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Collections.singletonMap("error", "El funcionario ya está asignado a este caso."));
                    }
                }

                nuevaMision.setFuncionario(funcionario);
                logger.info("Funcionario asignado: {}", funcionario);
            } else {
                logger.warn("No se encontró funcionario con cédula: {}", crearMisionDTO.getCedulaFuncionario());
            }
        } else {
            logger.warn("Cédula de funcionario no proporcionada en el DTO.");
        }

        // Relación con Caso
        if (crearMisionDTO.getNumeroCaso() != null && !crearMisionDTO.getNumeroCaso().isEmpty()) {
            logger.info("Buscando caso con código: {}", crearMisionDTO.getNumeroCaso());
            Optional<Caso> casoOptional = casoRepository.findByCodigoCaso(crearMisionDTO.getNumeroCaso());
            casoOptional.ifPresent(caso -> {
                nuevaMision.setCaso(caso);
                logger.info("Caso encontrado: {}", caso);
            });
            if (!casoOptional.isPresent()) {
                logger.warn("No se encontró caso con código: {}", crearMisionDTO.getNumeroCaso());
            }
        } else {
            logger.warn("Número de caso no proporcionado en el DTO.");
        }

        // Relación con Especialidad
        if (crearMisionDTO.getIdEspecialidad() != null) {
            logger.info("Buscando especialidad con ID: {}", crearMisionDTO.getIdEspecialidad());
            Optional<Especialidad> especialidadOptional = especialidadRepository.findById(crearMisionDTO.getIdEspecialidad());
            especialidadOptional.ifPresent(nuevaMision::setEspecialidad);
            if (!especialidadOptional.isPresent()) {
                logger.warn("No se encontró especialidad con ID: {}", crearMisionDTO.getIdEspecialidad());
            }
        } else {
            logger.info("ID de especialidad no proporcionado en el DTO.");
        }

        logger.info("Guardando misión en la base de datos: {}", nuevaMision);
        Mision misionGuardada = misionRepository.save(nuevaMision);
        logger.info("Misión guardada exitosamente con ID: {}", misionGuardada.getNumeroMision());

        return ResponseEntity.ok(misionGuardada);
    }
    @Transactional
    public void eliminarMision(Integer numeroMision) {
        misionRepository.findByNumeroMision(numeroMision)
                .ifPresent(mision -> {
                    mision.setActivo(false);
                    misionRepository.save(mision);
                });
    }

    @Transactional
    public void marcarMisionComoInactiva(Integer numeroMision) {
        misionRepository.findByNumeroMision(numeroMision)
                .ifPresent(mision -> {
                    mision.setActivo(false);
                    misionRepository.save(mision);
                });
    }

    public List<MisionDTO> obtenerTodasLasMisionesDTO() {
        return misionRepository.findAll().stream()
                .map(m -> new MisionDTO(
                        m.getNumeroMision(),
                        m.getFuncionario() != null ? m.getFuncionario().getIdFuncionario() : null,
                        m.getFuncionario() != null ? m.getFuncionario().getNombre() : "Sin Funcionario",
                        m.getFuncionario() != null ? m.getFuncionario().getApellido() : "Sin Funcionario",
                        m.getActividades(),
                        m.getCaso() != null ? m.getCaso().getCodigoCaso() : "Sin Caso",
                        m.getCaso() != null ? m.getCaso().getIdCaso() : null, // Aquí obtenemos el ID del caso
                        m.getActivo(),
                        m.getEspecialidad() != null ? m.getEspecialidad().getIdEspecialidad(): null,
                        m.getEspecialidad()!= null ? m.getEspecialidad().getEspecialidad():"Sin Especialidad"))
                .collect(Collectors.toList());
    }


    private String formatearMisionConInfo(Mision mision) {
        String nombreFuncionario = "Sin Funcionario";
        if (mision.getFuncionario() != null) {
            nombreFuncionario = mision.getFuncionario().getNombre();
        }

        String numeroCaso = "Sin Caso";
        if (mision.getCaso() != null) {
            numeroCaso = mision.getCaso().getCodigoCaso();
        }

        return String.format(
                "Número Misión: %d, Funcionario: %s, Actividad: %s, Número Caso: %s, Activo: %s",
                mision.getNumeroMision(),
                nombreFuncionario,
                mision.getActividades(),
                numeroCaso,
                mision.getActivo()
        );
    }

    @Transactional
    public Optional<Mision> actualizarMision(Integer numeroMision, MisionDTO misionDTO) {
        Optional<Mision> misionExistenteOptional = misionRepository.findByNumeroMision(numeroMision);
        if (misionExistenteOptional.isPresent()) {
            Mision misionExistente = misionExistenteOptional.get();

            if (misionDTO.getActividades() != null) {
                misionExistente.setActividades(misionDTO.getActividades());
            }
            if (misionDTO.getActivo() != null) {
                misionExistente.setActivo(misionDTO.getActivo());
            }


            if (misionDTO.getNumeroCaso() != null && !misionDTO.getNumeroCaso().isEmpty()) {
                Optional<Caso> casoOptional = casoRepository.findByCodigoCaso(misionDTO.getNumeroCaso());
                casoOptional.ifPresent(misionExistente::setCaso);
            }
            if (misionDTO.getIdEspecialidad() != null) {
                especialidadRepository.findById(misionDTO.getIdEspecialidad())
                        .ifPresent(misionExistente::setEspecialidad);
            }

            return Optional.of(misionRepository.save(misionExistente));
        }
        return Optional.empty();
    }

    public List<Mision> obtenerMisionesPorFuncionario(Funcionario funcionario) {
        return misionRepository.findByFuncionario(funcionario);
    }

    // O si solo tienes el ID del funcionario:
    public List<Mision> obtenerMisionesPorFuncionarioId(Integer funcionarioId) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(funcionarioId);
        return funcionarioOptional.map(misionRepository::findByFuncionario).orElse(Collections.emptyList());
    }
    public List<ReporteFuncionarioCasosDTO> obtenerReporteFuncionariosConCasos() {
        List<Mision> todasLasMisiones = misionRepository.findAll();

        Map<Funcionario, Set<String>> mapaFuncionarioCasos = new HashMap<>();

        for (Mision mision : todasLasMisiones) {
            Funcionario funcionario = mision.getFuncionario();
            Caso caso = mision.getCaso();

            if (funcionario != null && caso != null) {
                mapaFuncionarioCasos
                        .computeIfAbsent(funcionario, f -> new HashSet<>())
                        .add(caso.getCodigoCaso());
            }
        }

        return mapaFuncionarioCasos.entrySet().stream()
                .map(entry -> {
                    Funcionario f = entry.getKey();
                    List<String> casos = new ArrayList<>(entry.getValue());
                    return new ReporteFuncionarioCasosDTO(f.getNombre(), f.getApellido(), casos);
                })
                .collect(Collectors.toList());
    }

    public List<MisionDTO> obtenerTodasLasMisionesActivasDTO() {
        return misionRepository.findAll().stream()
                .filter(m -> m.getActivo() != null && m.getActivo())
                .map(m -> new MisionDTO(
                        m.getNumeroMision(),
                        m.getFuncionario() != null ? m.getFuncionario().getIdFuncionario() : null,
                        m.getFuncionario() != null ? m.getFuncionario().getNombre() : "Sin Funcionario",
                        m.getFuncionario() != null ? m.getFuncionario().getApellido() : "Sin Funcionario",
                        m.getActividades(),
                        m.getCaso() != null ? m.getCaso().getCodigoCaso() : "Sin Caso",
                        m.getCaso() != null ? m.getCaso().getIdCaso() : null, // Aquí obtenemos el ID del caso
                        m.getActivo(),
                        m.getEspecialidad() != null ? m.getEspecialidad().getIdEspecialidad(): null,
                        m.getEspecialidad()!= null ? m.getEspecialidad().getEspecialidad():"Sin Especialidad"))
                .collect(Collectors.toList());
    }

    public List<MisionDTO> obtenerTodasLasMisionesInactivasDTO() {
        return misionRepository.findAll().stream()
                .filter(m -> m.getActivo() != null && !m.getActivo())
                .map(m -> new MisionDTO(
                        m.getNumeroMision(),
                        m.getFuncionario() != null ? m.getFuncionario().getIdFuncionario() : null,
                        m.getFuncionario() != null ? m.getFuncionario().getNombre() : "Sin Funcionario",
                        m.getFuncionario() != null ? m.getFuncionario().getApellido() : "Sin Funcionario",
                        m.getActividades(),
                        m.getCaso() != null ? m.getCaso().getCodigoCaso() : "Sin Caso",
                        m.getCaso() != null ? m.getCaso().getIdCaso() : null, // Aquí obtenemos el ID del caso
                        m.getActivo(),
                        m.getEspecialidad() != null ? m.getEspecialidad().getIdEspecialidad(): null,
                        m.getEspecialidad()!= null ? m.getEspecialidad().getEspecialidad():"Sin Especialidad"))
                .collect(Collectors.toList());
    }

}