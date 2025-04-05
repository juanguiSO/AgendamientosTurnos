package com.agendamientos.agendamientosTurnos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // Devuelve 404 Not Found automáticamente
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Buena práctica

    public ResourceNotFoundException(String message) {
        super(message);
    }
}