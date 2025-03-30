package com.agendamientos.agendamientosTurnos.repository;


import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


    @Repository
    public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

        Optional<Funcionario> findByCedula(String cedula);

        // Solo devolver funcionarios activos
        List<Funcionario> findByActivoTrue();

        Funcionario findByCorreo(String correo);
        boolean existsByCorreo(String correo);
    }

