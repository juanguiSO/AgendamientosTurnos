package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.MisionDTO;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MisionService {

    private final MisionRepository misionRepository;

    @Autowired
    public MisionService(MisionRepository misionRepository) {
        this.misionRepository = misionRepository;
    }

    public Optional<Mision> obtenerMisionPorNumero(Integer numeroMision) {
        return misionRepository.findByNumeroMision(numeroMision);
    }


    public List<Mision> buscarMisionesPorActividad(String actividad) {
        return misionRepository.findByActividadesContaining(actividad);
    }

    public List<Mision> obtenerMisionesPorCaso(Integer idCaso) {
        return misionRepository.findByCaso_IdCaso(idCaso);
    }

    public List<Mision> obtenerMisionesActivas() {
        return misionRepository.findByActivoTrue();
    }

    public List<Mision> obtenerMisionesInactivas() {
        return misionRepository.findByActivoFalse();
    }

    public List<Mision> obtenerMisionesActivasPorCaso(Integer idCaso) {
        return misionRepository.findByCaso_IdCasoAndActivoTrue(idCaso);
    }

    public List<Mision> obtenerTodasLasMisiones() {
        return misionRepository.findAll();
    }

    public Optional<Mision> obtenerMisionPorId(Integer id) {
        return misionRepository.findById(id);
    }

    public Mision guardarMision(Mision mision) {
        return misionRepository.save(mision);
    }

    @Transactional
    public void eliminarMision(Integer numeroMision) {
        misionRepository.findByNumeroMision(numeroMision)
                .ifPresent(mision -> {
                    mision.setActivo(false);
                    misionRepository.save(mision);
                });
    }
    @Transactional
    public void marcarMisionComoInactiva(Integer numeroMision) {
        misionRepository.findByNumeroMision(numeroMision)
                .ifPresent(mision -> {
                    mision.setActivo(false);
                    misionRepository.save(mision);
                });
    }

    public List<MisionDTO> obtenerTodasLasMisionesDTO() {
        return misionRepository.findAll().stream()
                .map(m -> new MisionDTO(
                        m.getNumeroMision(),
                        m.getFuncionario() != null ? m.getFuncionario().getNombre() : "Sin Funcionario",
                        m.getFuncionario() != null ? m.getFuncionario().getApellido() : "Sin Funcionario",
                        m.getActividades(),
                        m.getCaso() != null ? m.getCaso().getCodigoCaso() : "Sin Caso",
                        m.getActivo()))
                .collect(Collectors.toList());
    }


    private String formatearMisionConInfo(Mision mision) {
        String nombreFuncionario = "Sin Funcionario";
        if (mision.getFuncionario() != null) {
            nombreFuncionario = mision.getFuncionario().getNombre();
        }

        String numeroCaso = "Sin Caso";
        if (mision.getCaso() != null) {
            numeroCaso = mision.getCaso().getCodigoCaso();
        }

        return String.format(
                "Número Misión: %d, Funcionario: %s, Actividad: %s, Número Caso: %s, Activo: %s",
                mision.getNumeroMision(),
                nombreFuncionario,
                mision.getActividades(),
                numeroCaso,
                mision.getActivo()
        );
    }
}