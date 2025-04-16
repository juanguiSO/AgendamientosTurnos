package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private MisionRepository misionRepository;

    public Map<Funcionario, List<Caso>> obtenerCasosPorFuncionario() {
        List<Object[]> resultados = misionRepository.findFuncionarioCasoAssignments();
        Map<Funcionario, List<Caso>> casosPorFuncionario = new HashMap<>();

        for (Object[] resultado : resultados) {
            Funcionario funcionario = (Funcionario) resultado[0];
            Caso caso = (Caso) resultado[1];

            // Agrupa los casos por funcionario
            casosPorFuncionario.computeIfAbsent(funcionario, k -> new ArrayList<>()).add(caso);
        }

        return casosPorFuncionario;
    }


    // Método para obtener las misiones de un funcionario por el objeto Funcionario
    public List<Mision> obtenerMisionesPorFuncionario(Funcionario funcionario) {
        return misionRepository.findByFuncionario(funcionario);
    }

    // Método para generar el PDF (lo implementaremos más adelante)
    public byte[] generarReportePdf(Map<Funcionario, List<Caso>> datos) {
        // Lógica para generar el PDF usando una librería como iText o Apache PDFBox
        // ...
        return null; // Temporalmente retorna null
    }

}