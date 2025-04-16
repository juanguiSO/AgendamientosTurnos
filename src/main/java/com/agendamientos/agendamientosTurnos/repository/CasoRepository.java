package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Integer> {

    List<Caso> findByActivoTrue();
    Optional<Caso> findByCodigoCaso(String codigoCaso);
    //List<Caso> findByFuncionario(Funcionario funcionario);
    // Asegúrate de tener ESTE método:
    Optional<Caso> findByIdCasoAndActivoTrue(Integer idCaso);

    List<Caso> findAll();

    Optional<Caso> findById(Integer idCaso);

    List<Caso> findByActivoFalse();
}