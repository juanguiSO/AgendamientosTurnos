package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.CasoDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Departamentos;
import com.agendamientos.agendamientosTurnos.entity.Municipio;
import com.agendamientos.agendamientosTurnos.repository.CasoRepository;
import com.agendamientos.agendamientosTurnos.repository.DepartamentosRepository;
import com.agendamientos.agendamientosTurnos.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CasoService {

    private final CasoRepository casoRepository;
    private final MunicipioRepository municipioRepository;
    private final DepartamentosRepository departamentosRepository;

    @Autowired
    public CasoService(CasoRepository casoRepository, MunicipioRepository municipioRepository, DepartamentosRepository departamentosRepository) {
        this.casoRepository = casoRepository;
        this.municipioRepository = municipioRepository;
        this.departamentosRepository = departamentosRepository;
    }

    public List<Caso> obtenerTodosLosCasosActivos() {
        return casoRepository.findByActivoTrue();
    }

    public Optional<Caso> obtenerCasoPorId(Integer idCaso) {
        return casoRepository.findById(idCaso);
    }

    public Caso guardarCaso(Caso caso) {
        caso.setActivo(true);
        return casoRepository.save(caso);
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

    // Nuevo método para obtener todos los casos como DTOs con nombres de municipio y departamento
    public List<CasoDTO> obtenerTodosLosCasosConNombres() {
        return casoRepository.findAll().stream()
                .map(caso -> {
                    String nombreMunicipio = "Sin Municipio";
                    if (caso.getIdMunicipio() != null) {
                        Optional<Municipio> municipioOptional = municipioRepository.findById(caso.getIdMunicipio());
                        nombreMunicipio = municipioOptional.map(Municipio::getMunicipio).orElse("Sin Municipio");
                    }

                    String nombreDepartamento = "Sin Departamento";
                    if (caso.getIdDepartamentos() != null) {
                        Optional<Departamentos> departamentosOptional = departamentosRepository().findById(caso.getIdDepartamentos());
                        nombreDepartamento = departamentosOptional.map(Departamentos::getDepartamento).orElse("Sin Departamento");
                    }

                    return new CasoDTO(

                            caso.getIdCaso(),
                            caso.getCodigoCaso(),
                            caso.getDelito(),
                            caso.getNombreDefensorPublico(),
                            caso.getNombreUsuarioVisitado(),
                            nombreDepartamento,
                            nombreMunicipio,
                            caso.getActivo()
                    );
                })
                .collect(Collectors.toList());
    }

    // Nuevo método para obtener casos activos como DTOs con nombres de municipio y departamento
    public List<CasoDTO> obtenerTodosLosCasosActivosConNombresDTO() {
        return casoRepository.findByActivoTrue().stream()
                .map(this::convertToCasoDTO)
                .collect(Collectors.toList());
    }

    // Método privado para convertir una entidad Caso a un CasoDTO
    private CasoDTO convertToCasoDTO(Caso caso) {
        String nombreMunicipio = "Sin Municipio";
        if (caso.getIdMunicipio() != null) {
            Optional<Municipio> municipioOptional = municipioRepository.findById(caso.getIdMunicipio());
            nombreMunicipio = municipioOptional.map(Municipio::getMunicipio).orElse("Sin Municipio");
        }

        String nombreDepartamento = "Sin Departamento";
        if (caso.getIdDepartamentos() != null) {
            Optional<Departamentos> departamentosOptional = departamentosRepository.findById(caso.getIdDepartamentos());
            nombreDepartamento = departamentosOptional.map(Departamentos::getDepartamento).orElse("Sin Departamento");
        }

        return new CasoDTO(

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

    // Métodos existentes que devuelven List<String> - se mantienen por ahora
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

    // Método privado para formatear la información del caso con los nombres
    private String formatearCasoConNombres(Caso caso) {
        String nombreMunicipio = "Sin Municipio";
        if (caso.getIdMunicipio() != null) {
            Optional<Municipio> municipioOptional = municipioRepository.findById(caso.getIdMunicipio());
            nombreMunicipio = municipioOptional.map(Municipio::getMunicipio).orElse("Sin Municipio");
        }

        String nombreDepartamento = "Sin Departamento";
        if (caso.getIdDepartamentos() != null) {
            Optional<Departamentos> departamentosOptional = departamentosRepository.findById(caso.getIdDepartamentos());
            nombreDepartamento = departamentosOptional.map(Departamentos::getDepartamento).orElse("Sin Departamento");
        }

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

    private DepartamentosRepository departamentosRepository() {
        return departamentosRepository;
    }

    @Transactional
    public Optional<CasoDTO> actualizarCaso(Integer idCaso, CasoDTO casoDTO) {
        Optional<Caso> casoExistenteOptional = casoRepository.findById(idCaso);
        if (casoExistenteOptional.isPresent()) {
            Caso casoExistente = casoExistenteOptional.get();

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

            // Buscar y establecer el ID del Municipio por nombre
            if (casoDTO.getNombreMunicipio() != null && !casoDTO.getNombreMunicipio().isEmpty() && !casoDTO.getNombreMunicipio().equals("Sin Municipio")) {
                municipioRepository.findByMunicipio(casoDTO.getNombreMunicipio())
                        .ifPresent(casoExistente::setIdMunicipio);
            } else if (casoDTO.getNombreMunicipio() != null && (casoDTO.getNombreMunicipio().isEmpty() || casoDTO.getNombreMunicipio().equals("Sin Municipio"))) {
                casoExistente.setIdMunicipio(null); // Desvincular municipio si se envía vacío o "Sin Municipio"
            }

            // Buscar y establecer el ID del Departamento por nombre
            if (casoDTO.getNombreDepartamento() != null && !casoDTO.getNombreDepartamento().isEmpty() && !casoDTO.getNombreDepartamento().equals("Sin Departamento")) {
                departamentosRepository.findByDepartamento(casoDTO.getNombreDepartamento())
                        .ifPresent(casoExistente::setIdDepartamentos);
            } else if (casoDTO.getNombreDepartamento() != null && (casoDTO.getNombreDepartamento().isEmpty() || casoDTO.getNombreDepartamento().equals("Sin Departamento"))) {
                casoExistente.setIdDepartamentos(null); // Desvincular departamento si se envía vacío o "Sin Departamento"
            }

            Caso casoActualizado = casoRepository.save(casoExistente);
            return Optional.of(convertToCasoDTO(casoActualizado));
        }
        return Optional.empty();
    }


}