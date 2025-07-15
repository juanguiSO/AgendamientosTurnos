package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Integer> {

    List<Caso> findByActivoTrue();

    List<Caso> findByActivo(Boolean activo);

    Optional<Caso> findByCodigoCaso(String codigoCaso);
    //List<Caso> findByFuncionario(Funcionario funcionario);
    // Asegúrate de tener ESTE método:
    Optional<Caso> findByIdCasoAndActivoTrue(Integer idCaso);

    List<Caso> findAll();

    Optional<Caso> findById(Integer idCaso);

    List<Caso> findByActivoFalse();
    /**
     * Busca y devuelve una lista de casos que están asociados a un Viaje específico.
     * Spring Data JPA generará automáticamente la consulta SQL para esto.
     *
     * @param viaje El objeto Viaje por el cual se buscarán los casos.
     * @return Una lista de objetos Caso asociados al Viaje dado.
     */
    List<Caso> findByViaje(Viaje viaje);
}