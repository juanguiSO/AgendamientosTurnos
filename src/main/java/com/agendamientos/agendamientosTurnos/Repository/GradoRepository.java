package com.agendamientos.agendamientosTurnos.Repository;

import com.agendamientos.agendamientosTurnos.Entity.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradoRepository extends JpaRepository<Grado,Long> {

}
