package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// No es necesario implementar esta interfaz, Spring Data JPA lo hace por ti
@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    // Puedes añadir métodos de consulta personalizados aquí si es necesario
    // Por ejemplo:
    // Roles findByName(String name);
}