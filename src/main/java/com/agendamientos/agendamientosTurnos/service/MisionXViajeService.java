package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.MisionNumeroDTO;
import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.MisionXViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisionXViajeService {
    private final MisionXViajeRepository repository;

    public MisionXViajeService(MisionXViajeRepository repository) {
        this.repository = repository;
    }

    public List<MisionXViaje> findAll() {
        return repository.findAll();
    }

    public Optional<MisionXViaje> findById(Long id) {
        return repository.findById(id);
    }

    public MisionXViaje save(MisionXViaje misionXViaje) {
        return repository.save(misionXViaje);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<MisionXViaje> findByViajeId(Integer idViaje) {
        return repository.findByViaje_IdViaje(idViaje);
    }


    public List<MisionNumeroDTO> findMisionNumeroDTOsByViajeId(Integer idViaje) {
        // Llama al método del repositorio que ahora carga las relaciones gracias al @EntityGraph
        List<MisionXViaje> misionesXViaje = repository.findByViaje_IdViaje(idViaje);

        // Mapea las entidades a los DTOs, accediendo a todas las propiedades necesarias
        return misionesXViaje.stream()
                .map(misionXViaje -> {
                    // Asegúrate de que las relaciones no sean nulas antes de acceder a ellas
                    Mision mision = misionXViaje.getMision();
                    Funcionario funcionario = (mision != null) ? mision.getFuncionario() : null;
                    Caso caso = (mision != null) ? mision.getCaso() : null;
                    Especialidad especialidadMision = (mision != null) ? mision.getEspecialidad() : null; // La especialidad está en Mision
                    Municipio municipio = (caso != null) ? caso.getMunicipio() : null;

                    // Construye los valores para el DTO, manejando posibles nulos de las relaciones
                    Integer numeroMision = (mision != null) ? mision.getNumeroMision() : null;
                    String nombreFuncionario = (funcionario != null) ? funcionario.getNombre() : "N/A";
                    String apellidoFuncionario = (funcionario != null) ? funcionario.getApellido() : "N/A";
                    // Para la especialidad, la obtenemos de la relación directa de Mision a Especialidad.
                    // Si 'especialidad' en la entidad Especialidad se llama 'nombreEspecialidad' o similar,
                    // ajusta el .getEspecialidad() de abajo. Tu entidad Especialidad la tiene como 'especialidad'.
                    String especialidad = (especialidadMision != null) ? especialidadMision.getEspecialidad() : "N/A";
                    String numeroCaso = (caso != null) ? caso.getCodigoCaso() : "N/A"; // Asumiendo que Caso tiene getCodigoCaso()
                    String actividadMision = (mision != null) ? mision.getActividades() : "N/A";
                    String nombreMunicipio = (municipio != null) ? municipio.getMunicipio() : "N/A";


                    return new MisionNumeroDTO(
                            numeroMision,
                            nombreFuncionario,
                            apellidoFuncionario,
                            especialidad,
                            numeroCaso,
                            actividadMision,
                            nombreMunicipio
                    );
                })
                .collect(Collectors.toList()); // Usar .toList() si estás en Java 16+
    }

}