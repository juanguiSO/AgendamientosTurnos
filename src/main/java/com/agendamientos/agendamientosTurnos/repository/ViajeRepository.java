package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que esta interfaz es un componente de Spring para acceso a datos
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {


    // List<Viaje> findByEstadoViaje_EstadoViaje(String estadoViaje); // Accede al atributo 'estadoViaje' dentro del objeto 'EstadoViaje'
}