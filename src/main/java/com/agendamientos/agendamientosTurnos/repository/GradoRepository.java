package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado,Long> {

    List<Grado> findByActivoTrue();  // Consulta personalizada para encontrar solo grados activos

}
