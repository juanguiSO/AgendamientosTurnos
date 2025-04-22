package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartamentosRepository extends JpaRepository<Departamento, Integer> {

    Optional<Departamento> findByDepartamento(String departamento);

}