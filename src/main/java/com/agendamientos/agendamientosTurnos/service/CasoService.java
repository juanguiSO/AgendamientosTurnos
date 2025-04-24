package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.CasoDTO;
import com.agendamientos.agendamientosTurnos.dto.CasoDetalleDTO;
import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.CasoRepository;
import com.agendamientos.agendamientosTurnos.repository.DepartamentosRepository;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import com.agendamientos.agendamientosTurnos.repository.MunicipioRepository;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository; // Import MisionRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CasoService {

    private final CasoRepository casoRepository;
    private final MunicipioRepository municipioRepository;
    private final DepartamentosRepository departamentosRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final MisionRepository misionRepository; // Inject MisionRepository

    @Autowired
    public CasoService(CasoRepository casoRepository, MunicipioRepository municipioRepository, DepartamentosRepository departamentosRepository, FuncionarioRepository funcionarioRepository, MisionRepository misionRepository) {
        this.casoRepository = casoRepository;
        this.municipioRepository = municipioRepository;
        this.departamentosRepository = departamentosRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.misionRepository = misionRepository;
    }

    public List<Caso> obtenerCasosPorFuncionarioATravesDeMisiones(Integer funcionarioId) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(funcionarioId);
        if (funcionarioOptional.isPresent()) {
            Funcionario funcionario = funcionarioOptional.get();
            List<Mision> misionesDelFuncionario = misionRepository.findByFuncionario(funcionario);
            List<Caso> casosAsignados = new ArrayList<>();
            for (Mision mision : misionesDelFuncionario) {
                if (mision.getCaso() != null) {
                    casosAsignados.add(mision.getCaso());
                }
            }
            return casosAsignados.stream().distinct().collect(Collectors.toList()); // Evitar duplicados
        }
        return Collections.emptyList();
    }

    public Optional<Caso> obtenerCasoPorId(Integer idCaso) {
        return casoRepository.findById(idCaso);
    }

    public Caso guardarCaso(CasoDTO casoDTO) {
        Caso caso = new Caso();
        caso.setActivo(casoDTO.getActivo());
        caso.setCodigoCaso(casoDTO.getCodigoCaso());
        caso.setDelito(casoDTO.getDelito());
        caso.setNombreDefensorPublico(casoDTO.getNombreDefensorPublico());
        caso.setNombreUsuarioVisitado(casoDTO.getNombreUsuarioVisitado());

        // Buscar y establecer las entidades Municipio y Departamentos por ID
        if (casoDTO.getIdMunicipio() != null) {
            Optional<Municipio> municipioOptional = municipioRepository.findById(casoDTO.getIdMunicipio());
            municipioOptional.ifPresent(caso::setMunicipio);
        }
        if (casoDTO.getIdDepartamento() != null) {
            Optional<Departamento> departamentoOptional = departamentosRepository.findById(casoDTO.getIdDepartamento());
            departamentoOptional.ifPresent(caso::setDepartamento);
        }

        Caso casoGuardado = casoRepository.save(caso);

        return casoGuardado;
    }

    @Transactional
    public void eliminarCaso(Integer idCaso) {
        Optional<Caso> casoOptional = casoRepository.findById(idCaso);
        casoOptional.ifPresent(caso -> {
            caso.setActivo(false);
            casoRepository.save(caso);
        });
    }

    public List<Caso> obtenerTodosLosCasos() {
        return casoRepository.findAll();
    }

    public List<CasoDTO> obtenerTodosLosCasosConNombres() {
        return casoRepository.findAll().stream()
                .map(this::convertToCasoDTO)
                .collect(Collectors.toList());
    }

    public List<CasoDTO> obtenerTodosLosCasosActivosConNombresDTO() {
        return casoRepository.findByActivoTrue().stream()
                .map(this::convertToCasoDTO)
                .collect(Collectors.toList());
    }



    public List<String> obtenerTodosLosCasosConNombresString() {
        return casoRepository.findAll().stream()
                .map(this::formatearCasoConNombres)
                .collect(Collectors.toList());
    }

    public List<String> obtenerTodosLosCasosActivosConNombresString() {
        return casoRepository.findByActivoTrue().stream()
                .map(this::formatearCasoConNombres)
                .collect(Collectors.toList());
    }

    private String formatearCasoConNombres(Caso caso) {
        String nombreMunicipio = caso.getMunicipio() != null ? caso.getMunicipio().getMunicipio() : "Sin Municipio";
        String nombreDepartamento = caso.getDepartamento() != null ? caso.getDepartamento().getDepartamento() : "Sin Departamento";

        return String.format(
                "ID: %d, Código: %s, Delito: %s, Defensor: %s, Usuario Visitado: %s, Departamento: %s, Municipio: %s, Activo: %s",
                caso.getIdCaso(),
                caso.getCodigoCaso(),
                caso.getDelito(),
                caso.getNombreDefensorPublico(),
                caso.getNombreUsuarioVisitado(),
                nombreDepartamento,
                nombreMunicipio,
                caso.getActivo()
        );
    }

    public MunicipioRepository getMunicipioRepository() {
        return municipioRepository;
    }

    public DepartamentosRepository getDepartamentosRepository() {
        return departamentosRepository;
    }

    @Transactional
    public Optional<CasoDTO> actualizarCaso(Integer idCaso, CasoDTO casoDTO) {
        Optional<Caso> casoExistenteOptional = casoRepository.findById(idCaso);
        if (casoExistenteOptional.isPresent()) {
            Caso casoExistente = casoExistenteOptional.get();

            // Realizar validaciones
            if (casoDTO.getCodigoCaso() != null && casoDTO.getCodigoCaso().trim().isEmpty()) {
                throw new IllegalArgumentException("El código del caso no puede estar vacío.");
            }
            if (casoDTO.getDelito() != null && casoDTO.getDelito().trim().isEmpty()) {
                throw new IllegalArgumentException("El delito no puede estar vacío.");
            }
            if (casoDTO.getNombreDefensorPublico() != null && casoDTO.getNombreDefensorPublico().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del defensor público no puede estar vacío.");
            }
            if (casoDTO.getNombreUsuarioVisitado() != null && casoDTO.getNombreUsuarioVisitado().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del usuario visitado no puede estar vacío.");
            }
            if (casoDTO.getIdMunicipio() != null && casoDTO.getIdMunicipio() <= 0) {
                throw new IllegalArgumentException("El ID del municipio debe ser un valor positivo.");
            }
            if (casoDTO.getIdDepartamento() != null && casoDTO.getIdDepartamento() <= 0) {
                throw new IllegalArgumentException("El ID del departamento debe ser un valor positivo.");
            }

            // Actualizar los campos solo si la validación pasa o si el campo no se proporciona
            if (casoDTO.getCodigoCaso() != null) {
                casoExistente.setCodigoCaso(casoDTO.getCodigoCaso());
            }
            if (casoDTO.getDelito() != null) {
                casoExistente.setDelito(casoDTO.getDelito());
            }
            if (casoDTO.getNombreDefensorPublico() != null) {
                casoExistente.setNombreDefensorPublico(casoDTO.getNombreDefensorPublico());
            }
            if (casoDTO.getNombreUsuarioVisitado() != null) {
                casoExistente.setNombreUsuarioVisitado(casoDTO.getNombreUsuarioVisitado());
            }
            if (casoDTO.getActivo() != null) {
                casoExistente.setActivo(casoDTO.getActivo());
            }

            // Buscar y establecer las entidades Municipio y Departamentos por ID
            if (casoDTO.getIdMunicipio() != null) {
                municipioRepository.findById(casoDTO.getIdMunicipio())
                        .ifPresentOrElse(
                                casoExistente::setMunicipio,
                                () -> System.out.println("Municipio no encontrado con ID: " + casoDTO.getIdMunicipio())
                        );
            }
            if (casoDTO.getIdDepartamento() != null) {
                departamentosRepository.findById(casoDTO.getIdDepartamento())
                        .ifPresentOrElse(
                                casoExistente::setDepartamento,
                                () -> System.out.println("Departamento no encontrado con ID: " + casoDTO.getIdDepartamento())
                        );
            }

            Caso casoActualizado = casoRepository.save(casoExistente);
            return Optional.of(convertToCasoDTO(casoActualizado));
        }
        return Optional.empty();
    }
    // Método para convertir de DTO a Entidad
    private Caso convertToEntity(CasoDTO casoDTO) {
        Caso caso = new Caso();
        caso.setActivo(casoDTO.getActivo());
        caso.setCodigoCaso(casoDTO.getCodigoCaso());
        caso.setDelito(casoDTO.getDelito());
        caso.setNombreDefensorPublico(casoDTO.getNombreDefensorPublico());
        caso.setNombreUsuarioVisitado(casoDTO.getNombreUsuarioVisitado());

        if (casoDTO.getIdMunicipio() != null) {
            municipioRepository.findById(casoDTO.getIdMunicipio())
                    .ifPresent(caso::setMunicipio);
        }
        if (casoDTO.getIdDepartamento() != null) {
            departamentosRepository.findById(casoDTO.getIdDepartamento())
                    .ifPresent(caso::setDepartamento);
        }
        return caso;
    }
    private CasoDTO convertToCasoDTO(Caso caso) {
        Integer idMunicipio = caso.getMunicipio() != null ? caso.getMunicipio().getIdMunicipio() : null;
        Integer idDepartamento = caso.getDepartamento() != null ? caso.getDepartamento().getIdDepartamentos() : null;

        return new CasoDTO(
                caso.getIdCaso(),
                caso.getCodigoCaso(),
                caso.getDelito(),
                caso.getNombreDefensorPublico(),
                caso.getNombreUsuarioVisitado(),
                idDepartamento, // <-- Asegúrate de pasar el ID aquí
                idMunicipio,    // <-- Asegúrate de pasar el ID aquí
                caso.getActivo()
        );
    }

    public List<CasoDetalleDTO> obtenerTodosLosCasosConDetalle() {
        List<Caso> casos = casoRepository.findAll();
        return casos.stream()
                .map(this::convertToCasoDetalleDTO)
                .collect(Collectors.toList());
    }

    private CasoDetalleDTO convertToCasoDetalleDTO(Caso caso) {
        CasoDetalleDTO dto = new CasoDetalleDTO();
        dto.setIdCaso(caso.getIdCaso());
        dto.setCodigoCaso(caso.getCodigoCaso());
        dto.setDelito(caso.getDelito());
        dto.setNombreDefensorPublico(caso.getNombreDefensorPublico());
        dto.setNombreUsuarioVisitado(caso.getNombreUsuarioVisitado());
        dto.setActivo(caso.getActivo());

        // Set both the ID and name of the department
        if (caso.getDepartamento() != null) {
            Departamento departamento = departamentosRepository.findById(caso.getDepartamento().getIdDepartamentos()).orElse(null);
            dto.setIdDepartamento(departamento != null ? departamento.getIdDepartamentos() : null);
            dto.setNombreDepartamento(departamento != null ? departamento.getDepartamento() : "N/A");
        } else {
            dto.setIdDepartamento(null);
            dto.setNombreDepartamento("N/A");
        }

        // Set both the ID and name of the municipality
        if (caso.getMunicipio() != null) {
            Municipio municipio = municipioRepository.findById(caso.getMunicipio().getIdMunicipio()).orElse(null);
            dto.setIdMunicipio(municipio != null ? municipio.getIdMunicipio() : null);
            dto.setNombreMunicipio(municipio != null ? municipio.getMunicipio() : "N/A");
        } else {
            dto.setIdMunicipio(null);
            dto.setNombreMunicipio("N/A");
        }

        return dto;
    }
}