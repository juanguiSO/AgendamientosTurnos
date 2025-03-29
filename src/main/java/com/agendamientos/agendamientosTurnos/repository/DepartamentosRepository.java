package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Departamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartamentosRepository extends JpaRepository<Departamentos, Integer> {

}