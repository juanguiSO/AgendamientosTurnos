package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Cargo;
import com.agendamientos.agendamientosTurnos.entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    List<Cargo> findByActivoTrue();  // Consulta personalizada para encontrar solo grados activos
}
