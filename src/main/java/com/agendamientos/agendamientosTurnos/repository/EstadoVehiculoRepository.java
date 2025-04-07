package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.EstadoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoVehiculoRepository extends JpaRepository<EstadoVehiculo, Integer> {

    List<EstadoVehiculo> findAll();
}
