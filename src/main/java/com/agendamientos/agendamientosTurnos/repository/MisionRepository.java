package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Mision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisionRepository extends JpaRepository<Mision, Integer> {

    // Métodos personalizados basados en los campos de la entidad Mision

    Optional<Mision> findByNumeroMision(Integer numeroMision);



    List<Mision> findByActividadesContaining(String actividad); // Busca misiones donde la actividad contenga la cadena

    List<Mision> findByCaso_IdCaso(Integer idCaso); // Busca misiones asociadas a un ID de Caso específico

    List<Mision> findByActivoTrue();

    List<Mision> findByActivoFalse();

    // Puedes combinar criterios
    List<Mision> findByCaso_IdCasoAndActivoTrue(Integer idCaso);

    // Spring Data JPA genera la implementación de estos métodos automáticamente
}