package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.entity.MisionXViaje;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MisionXViajeRepository extends JpaRepository<MisionXViaje, Long> {

    int deleteByViaje(Viaje viaje);
    List<MisionXViaje> findByMision_NumeroMision(Long numeroMision);

    @EntityGraph(attributePaths = {"mision", "mision.funcionario", "mision.caso", "mision.especialidad"})
    List<MisionXViaje> findByViaje_IdViaje(Integer idViaje);


    Optional<MisionXViaje> findByMision(Mision mision);

    List<MisionXViaje> findByViaje(Viaje viaje);

}