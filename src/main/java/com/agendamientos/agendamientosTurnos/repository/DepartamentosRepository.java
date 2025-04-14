package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Departamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartamentosRepository extends JpaRepository<Departamentos, Integer> {

    Optional<Departamentos> findByDepartamento(String departamento);

}