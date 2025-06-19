package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.EstadoViaje;
import com.agendamientos.agendamientosTurnos.entity.Viaje;
import com.agendamientos.agendamientosTurnos.entity.Mision;         // Importar la entidad Mision
import com.agendamientos.agendamientosTurnos.entity.MisionXViaje; // Importar la entidad MisionXViaje
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.ViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;     // Necesitas MisionRepository para guardar Mision
import com.agendamientos.agendamientosTurnos.repository.MisionXViajeRepository; // Importar el repositorio de MisionXViaje
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
    private final MisionRepository misionRepository; // Sigue siendo necesario para guardar los cambios en Mision
    private final MisionXViajeRepository misionXViajeRepository; // Inyectar MisionXViajeRepository

    // IDs de los estados de viaje
    private static final Integer ESTADO_PROGRAMADO_ID = 1;
    private static final Integer ESTADO_FINALIZADO_ID = 4;

    @Autowired
    public ViajeSchedulerService(ViajeRepository viajeRepository,
                                 EstadoViajeRepository estadoViajeRepository,
                                 MisionRepository misionRepository, // Añadir MisionRepository
                                 MisionXViajeRepository misionXViajeRepository) { // Añadir MisionXViajeRepository
        this.viajeRepository = viajeRepository;
        this.estadoViajeRepository = estadoViajeRepository;
        this.misionRepository = misionRepository;
        this.misionXViajeRepository = misionXViajeRepository; // Asignar
    }

    /**
     * Tarea programada para actualizar automáticamente el estado de los viajes y sus misiones asociadas.
     */
    @Scheduled(fixedRate = 300000) // Se ejecuta cada 5 minutos
    @Transactional
    public void actualizarEstadoViajesYMisiones() {
        System.out.println("Iniciando tarea programada: Actualizando estados de viajes a Finalizado y misiones asociadas...");

        Optional<EstadoViaje> estadoProgramadoOptional = estadoViajeRepository.findById(ESTADO_PROGRAMADO_ID);
        if (estadoProgramadoOptional.isEmpty()) {
            System.err.println("Error: No se encontró el estado de viaje 'Programado' con ID " + ESTADO_PROGRAMADO_ID);
            return;
        }
        EstadoViaje estadoProgramado = estadoProgramadoOptional.get();

        Optional<EstadoViaje> estadoFinalizadoOptional = estadoViajeRepository.findById(ESTADO_FINALIZADO_ID);
        if (estadoFinalizadoOptional.isEmpty()) {
            System.err.println("Error: No se encontró el estado de viaje 'Finalizado' con ID " + ESTADO_FINALIZADO_ID);
            return;
        }
        EstadoViaje estadoFinalizado = estadoFinalizadoOptional.get();

        List<Viaje> viajesProgramados = viajeRepository.findByEstadoViaje(estadoProgramado);

        int viajesActualizados = 0;
        int misionesInactivadas = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Viaje viaje : viajesProgramados) {
            if (viaje.getTiempoFin() != null && viaje.getTiempoFin().isBefore(now)) {
                // 1. Actualizar el estado del viaje a "Finalizado"
                viaje.setEstadoViaje(estadoFinalizado);
                viajeRepository.save(viaje);
                viajesActualizados++;
                System.out.println("Viaje ID " + viaje.getIdViaje() + " actualizado a 'Finalizado'.");

                // 2. Inactivar las misiones asociadas a este viaje a través de MisionXViaje
                List<MisionXViaje> relacionesMisionViaje = misionXViajeRepository.findByViaje(viaje);

                for (MisionXViaje relacion : relacionesMisionViaje) {
                    Mision mision = relacion.getMision(); // Obtenemos el objeto Mision de la relación
                    if (mision != null && mision.getActivo()) { // Verificamos si la misión existe y está activa
                        mision.setActivo(false); // Inactivamos la misión
                        misionRepository.save(mision); // Guardamos el cambio en la misión
                        misionesInactivadas++;
                        System.out.println("Misión ID " + mision.getNumeroMision() + " (asociada vía Viaje ID " + viaje.getIdViaje() + ") inactivada.");
                    }
                }
            }
        }

        System.out.println("Tarea programada finalizada. Viajes actualizados: " + viajesActualizados + ". Misiones inactivadas: " + misionesInactivadas);
    }
}