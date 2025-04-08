package com.agendamientos.agendamientosTurnos.repository;

import com.agendamientos.agendamientosTurnos.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    List<Vehiculo> findByMarcaAndActivoTrue(String marca); // Buscar solo vehículos activos por marca

    List<Vehiculo> findByModeloAndPlacaAndActivoTrue(String modelo, String placa); // Buscar solo vehículos activos por modelo y placa

    List<Vehiculo> findByEstadoVehiculo_EstadoVehiculoAndActivoTrue(String estado); // Buscar solo vehículos activos por estado (nombre)

    List<Vehiculo> findByEstadoVehiculo_IdEstadoVehiculoAndActivoTrue(Integer idEstado); // Buscar solo vehículos activos por estado (ID)

    Optional<Vehiculo> findByPlacaAndActivoTrue(String placa); // Buscar solo vehículos activos por placa

    Optional<Vehiculo> findByPlaca(String placa); // Buscar por placa incluyendo los inactivos (para la "eliminación")

    List<Vehiculo> findByActivoTrue(); // Buscar todos los vehículos activos

    // Nuevo método para listar todos los vehículos (activos e inactivos)
    List<Vehiculo> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Vehiculo v SET v.activo = false WHERE v.placa = :placa")
    void marcarComoInactivoPorPlaca(String placa);
}