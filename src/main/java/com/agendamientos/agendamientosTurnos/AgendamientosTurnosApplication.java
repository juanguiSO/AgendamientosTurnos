package com.agendamientos.agendamientosTurnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgendamientosTurnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamientosTurnosApplication.class, args);
	}

}
