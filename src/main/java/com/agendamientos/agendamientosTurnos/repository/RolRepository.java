package com.agendamientos.agendamientosTurnos.repository; // Asegúrate de que el paquete sea el correcto

import com.agendamientos.agendamientosTurnos.entity.Rol; // Importa tu entidad Rol
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Buena práctica para métodos findBy... que pueden no devolver nada

/**
 * Repositorio Spring Data JPA para la entidad Rol.
 * Proporciona métodos CRUD estándar y permite definir consultas personalizadas.
 */
@Repository // Indica que es un componente de repositorio gestionado por Spring
public interface RolRepository extends JpaRepository<Rol, Integer> { // <TipoDeEntidad, TipoDeLaClavePrimaria>

    /**
     * Busca un Rol por su nombre.
     * Spring Data JPA implementará automáticamente este método basándose en su nombre.
     *
     * @param nombre El nombre del Rol a buscar.
     * @return Un Optional que contiene el Rol si se encuentra, o un Optional vacío si no.
     */
    Optional<Rol> findByNombre(String nombre);


}