package com.agendamientos.agendamientosTurnos.repository;


import com.agendamientos.agendamientosTurnos.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// No es necesario implementar esta interfaz, Spring Data JPA lo hace por ti
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    Optional<Municipio> findByMunicipio(String municipio);
    // List<Municipio> findByMunicipioStartingWith(String prefix);
}