package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    Optional<Municipio> findByMunicipio(String municipio);

    // Nuevo método para buscar municipios por el ID del departamento
    List<Municipio> findByIdDepartamento(Integer idDepartamento);

    // También podrías querer buscar por nombre de municipio y ID de departamento
    Optional<Municipio> findByMunicipioAndIdDepartamento(String municipio, Integer idDepartamento);

    // Otros métodos de consulta que necesites...
}