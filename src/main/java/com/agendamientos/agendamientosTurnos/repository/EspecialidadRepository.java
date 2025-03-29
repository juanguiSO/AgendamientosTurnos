package com.agendamientos.agendamientosTurnos.repository;


import com.agendamientos.agendamientosTurnos.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    List<Especialidad> findByActivoTrue();  // Consulta personalizada para encontrar solo especialidades activas
}
