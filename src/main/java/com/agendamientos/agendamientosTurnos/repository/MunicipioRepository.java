package com.agendamientos.agendamientosTurnos.repository;


import com.agendamientos.agendamientosTurnos.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// No es necesario implementar esta interfaz, Spring Data JPA lo hace por ti
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    // List<Municipio> findByMunicipioStartingWith(String prefix);
}