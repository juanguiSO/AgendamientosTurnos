package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ViajeSchedulerService {

    private final ViajeRepository viajeRepository;
    private final EstadoViajeRepository estadoViajeRepository;

    // IDs de los estados de viaje según tu base de datos (ver imagen proporcionada)
    private static final Integer ESTADO_PROGRAMADO_ID = 1;
    private static final Integer ESTADO_FINALIZADO_ID = 4;

    @Autowired
    public ViajeSchedulerService(ViajeRepository viajeRepository, EstadoViajeRepository estadoViajeRepository) {
        this.viajeRepository = viajeRepository;
        this.estadoViajeRepository = estadoViajeRepository;
    }

    /**
     * Tarea programada para actualizar automáticamente el estado de los viajes.
     * Se ejecuta cada 5 minutos (300000 ms).
     * Puedes ajustar la frecuencia según tus necesidades.
     */
    @Scheduled(fixedRate = 300000) // Se ejecuta cada 5 minutos
    @Transactional // Asegura que las operaciones de base de datos se manejen en una transacción
    public void actualizarEstadoViajesFinalizados() {
        System.out.println("Iniciando tarea programada: Actualizando estados de viajes a Finalizado...");

        // 1. Obtener el objeto EstadoViaje "Programado"
        Optional<EstadoViaje> estadoProgramadoOptional = estadoViajeRepository.findById(ESTADO_PROGRAMADO_ID);
        if (estadoProgramadoOptional.isEmpty()) {
            System.err.println("Error: No se encontró el estado de viaje 'Programado' con ID " + ESTADO_PROGRAMADO_ID);
            return;
        }
        EstadoViaje estadoProgramado = estadoProgramadoOptional.get();

        // 2. Obtener el objeto EstadoViaje "Finalizado"
        Optional<EstadoViaje> estadoFinalizadoOptional = estadoViajeRepository.findById(ESTADO_FINALIZADO_ID);
        if (estadoFinalizadoOptional.isEmpty()) {
            System.err.println("Error: No se encontró el estado de viaje 'Finalizado' con ID " + ESTADO_FINALIZADO_ID);
            return;
        }
        EstadoViaje estadoFinalizado = estadoFinalizadoOptional.get();

        // 3. Buscar todos los viajes que están en estado "Programado"
        // Necesitarás un método en ViajeRepository para buscar por EstadoViaje
        List<Viaje> viajesProgramados = viajeRepository.findByEstadoViaje(estadoProgramado);

        int viajesActualizados = 0;
        LocalDateTime now = LocalDateTime.now();

        // 4. Iterar sobre los viajes programados y verificar si ya finalizaron
        for (Viaje viaje : viajesProgramados) {
            // Si el tiempo de fin del viaje ha pasado (es anterior o igual a la hora actual)
            if (viaje.getTiempoFin() != null && viaje.getTiempoFin().isBefore(now)) {
                viaje.setEstadoViaje(estadoFinalizado); // Cambiar el estado a "Finalizado"
                viajeRepository.save(viaje); // Guardar el cambio en la base de datos
                viajesActualizados++;
                System.out.println("Viaje ID " + viaje.getIdViaje() + " actualizado a 'Finalizado'.");
            }
        }

        System.out.println("Tarea programada finalizada. Viajes actualizados: " + viajesActualizados);
    }
}
