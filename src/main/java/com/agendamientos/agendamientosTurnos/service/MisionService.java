package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.dto.MisionDTO;
import com.agendamientos.agendamientosTurnos.dto.ReporteFuncionarioCasosDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.repository.CasoRepository;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MisionService {

    private final MisionRepository misionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final CasoRepository casoRepository;

    @Autowired
    public MisionService(MisionRepository misionRepository, FuncionarioRepository funcionarioRepository, CasoRepository casoRepository) {
        this.misionRepository = misionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.casoRepository = casoRepository;
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

    @Transactional
    public Optional<Mision> actualizarMision(Integer numeroMision, MisionDTO misionDTO) {
        Optional<Mision> misionExistenteOptional = misionRepository.findByNumeroMision(numeroMision);
        if (misionExistenteOptional.isPresent()) {
            Mision misionExistente = misionExistenteOptional.get();

            if (misionDTO.getActividades() != null) {
                misionExistente.setActividades(misionDTO.getActividades());
            }
            if (misionDTO.getActivo() != null) {
                misionExistente.setActivo(misionDTO.getActivo());
            }

            // Manejar la relación con Funcionario por ID
            if (misionDTO.getIdFuncionario() != null) {
                Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(misionDTO.getIdFuncionario());
                funcionarioOptional.ifPresent(misionExistente::setFuncionario);
            }

            if (misionDTO.getNumeroCaso() != null && !misionDTO.getNumeroCaso().isEmpty()) {
                Optional<Caso> casoOptional = casoRepository.findByCodigoCaso(misionDTO.getNumeroCaso());
                casoOptional.ifPresent(misionExistente::setCaso);
            }

            return Optional.of(misionRepository.save(misionExistente));
        }
        return Optional.empty();
    }

    public List<Mision> obtenerMisionesPorFuncionario(Funcionario funcionario) {
        return misionRepository.findByFuncionario(funcionario);
    }

    // O si solo tienes el ID del funcionario:
    public List<Mision> obtenerMisionesPorFuncionarioId(Integer funcionarioId) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(funcionarioId);
        return funcionarioOptional.map(misionRepository::findByFuncionario).orElse(Collections.emptyList());
    }
    public List<ReporteFuncionarioCasosDTO> obtenerReporteFuncionariosConCasos() {
        List<Mision> todasLasMisiones = misionRepository.findAll();

        Map<Funcionario, Set<String>> mapaFuncionarioCasos = new HashMap<>();

        for (Mision mision : todasLasMisiones) {
            Funcionario funcionario = mision.getFuncionario();
            Caso caso = mision.getCaso();

            if (funcionario != null && caso != null) {
                mapaFuncionarioCasos
                        .computeIfAbsent(funcionario, f -> new HashSet<>())
                        .add(caso.getCodigoCaso());
            }
        }

        return mapaFuncionarioCasos.entrySet().stream()
                .map(entry -> {
                    Funcionario f = entry.getKey();
                    List<String> casos = new ArrayList<>(entry.getValue());
                    return new ReporteFuncionarioCasosDTO(f.getNombre(), f.getApellido(), casos);
                })
                .collect(Collectors.toList());
    }


}

