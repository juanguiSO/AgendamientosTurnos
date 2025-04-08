package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Caso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Integer> {

    List<Caso> findByActivoTrue();

    // Asegúrate de tener ESTE método:
    Optional<Caso> findByIdCasoAndActivoTrue(Integer idCaso);

    List<Caso> findAll();

    Optional<Caso> findById(Integer idCaso);

    List<Caso> findByActivoFalse();
}