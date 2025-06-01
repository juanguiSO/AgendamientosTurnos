package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.MisionRepository;
import com.agendamientos.agendamientosTurnos.repository.ViajeRepository;
import com.agendamientos.agendamientosTurnos.util.PdfGeneratorUtil;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;


import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ReporteService {

    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private ViajeRepository viajeRepository;
    @Autowired
    private EstadoViajeRepository estadoViajeRepository;

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
    public byte[] generarReporteViajesPorEstado(Integer idEstadoViaje) throws IOException {
        Optional<EstadoViaje> estadoViajeOptional = estadoViajeRepository.findById(idEstadoViaje);
        if (estadoViajeOptional.isEmpty()) {
            // Manejo de error si el estado de viaje no existe
            throw new IllegalArgumentException("Estado de Viaje con ID " + idEstadoViaje + " no encontrado.");
        }
        EstadoViaje estadoViaje = estadoViajeOptional.get();

        List<Viaje> viajes = viajeRepository.findByEstadoViaje(estadoViaje);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = PdfGeneratorUtil.initializePdfDocument(outputStream, true); // true para A4 horizontal
        PdfFont customFont = PdfGeneratorUtil.getCustomFont();
        DeviceRgb customHeaderColor = PdfGeneratorUtil.getHeaderColor();

        // Título del reporte
        document.add(new Paragraph("Reporte de Viajes - Estado: " + estadoViaje.getEstadoViaje())
                .setFont(customFont)
                .setFontSize(20)
                .setBold()
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        if (viajes.isEmpty()) {
            document.add(new Paragraph("No hay viajes con el estado '" + estadoViaje.getEstadoViaje()+ "'.")
                    .setFont(customFont)
                    .setFontSize(12)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
        } else {
            // Tabla de viajes
            Table table = new Table(new float[]{1.5f, 2, 2, 1.5f, 2, 1.5f, 1f}); // Anchos de columna
            table.setWidth(UnitValue.createPercentValue(100)); // Ocupar todo el ancho de la página

            // Encabezados de la tabla
            table.addHeaderCell(new Cell().add(new Paragraph("ID Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Inicio").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Fin").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Distancia (Km)").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Vehículo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Estado").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addHeaderCell(new Cell().add(new Paragraph("Activo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Viaje viaje : viajes) {
                // Filas de datos
                table.addCell(new Cell().add(new Paragraph(viaje.getIdViaje().toString()).setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(viaje.getTiempoInicio().toString()).setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(viaje.getTiempoFin().toString()).setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", viaje.getDistanciaRecorrida())).setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(viaje.getVehiculo() != null ? viaje.getVehiculo().getPlaca() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(viaje.getEstadoViaje() != null ? viaje.getEstadoViaje().getEstadoViaje() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(viaje.isActivo() ? "Sí" : "No").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));

                alternateColor = !alternateColor;
            }
            document.add(table);
        }

        document.close();
        return outputStream.toByteArray();
    }
}