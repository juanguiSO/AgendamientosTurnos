package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Cargo;
import com.agendamientos.agendamientosTurnos.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    @Autowired
    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    public List<Cargo> getAllCargos() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> getCargoById(Integer id) {
        return cargoRepository.findById(id);
    }

    public Cargo createCargo(Cargo cargo) {
        // Aquí puedes agregar lógica de validación antes de guardar
        return cargoRepository.save(cargo);
    }

    public Cargo updateCargo(Integer id, Cargo cargoDetails) {
        Optional<Cargo> cargoOptional = cargoRepository.findById(id);
        if (cargoOptional.isPresent()) {
            Cargo cargo = cargoOptional.get();
            cargo.setNombreCargo(cargoDetails.getNombreCargo());
            cargo.setActivo(cargoDetails.getActivo()); // Actualiza el estado activo
            return cargoRepository.save(cargo);
        } else {
            // Manejar el caso en que el Cargo no existe (lanzar excepción, retornar null, etc.)
            return null; // o lanzar una excepción personalizada
        }
    }

    public void deleteCargo(Integer id) {
        cargoRepository.deleteById(id);
    }
}