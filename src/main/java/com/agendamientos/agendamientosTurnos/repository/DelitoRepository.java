package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Delito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelitoRepository extends JpaRepository<Delito, Integer> {
    // Aquí puedes agregar métodos personalizados si los necesitas
}