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
}