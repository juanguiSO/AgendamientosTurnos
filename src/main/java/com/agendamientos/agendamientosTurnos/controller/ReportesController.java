package com.agendamientos.agendamientosTurnos.controller;

// En tu controlador REST en Spring

import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.service.CasoService;
import com.agendamientos.agendamientosTurnos.service.ReporteService;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendamientos.agendamientosTurnos.service.MisionService;
import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO;
import com.agendamientos.agendamientosTurnos.service.FuncionarioService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    private final FuncionarioService funcionarioService;
    private final MisionService misionService;
    private final ReporteService reporteService;
    private final CasoService casoService;



    @Autowired
    public ReportesController(FuncionarioService funcionarioService, MisionService misionService, ReporteService reporteService,CasoService casoService) {
        this.funcionarioService = funcionarioService;
        this.misionService = misionService;
        this.reporteService = reporteService;
        this.casoService = casoService;


    }


    private ResponseEntity<byte[]> generarReporte(String disposition) throws Exception {
        List<FuncionarioDTO> funcionarios = funcionarioService.getAllFuncionarios();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf);

        Table table = new Table(new float[]{2, 2, 2, 2, 2, 1, 2, 2});

        // Añadir encabezados de la tabla
        table.addCell(new Cell().add(new Paragraph("Cédula")));
        table.addCell(new Cell().add(new Paragraph("Nombre")));
        table.addCell(new Cell().add(new Paragraph("Apellido")));
//        table.addCell(new Cell().add(new Paragraph("Correo")));
        table.addCell(new Cell().add(new Paragraph("Teléfono")));
        table.addCell(new Cell().add(new Paragraph("Especialidad")));
        table.addCell(new Cell().add(new Paragraph("G")));
//        table.addCell(new Cell().add(new Paragraph("Activo")));
        table.addCell(new Cell().add(new Paragraph("Cargo")));
        table.addCell(new Cell().add(new Paragraph("Rol")));

        // Añadir datos de los funcionarios
        for (FuncionarioDTO funcionario : funcionarios) {
            table.addCell(new Cell().add(new Paragraph(funcionario.getCedula())));
            table.addCell(new Cell().add(new Paragraph(funcionario.getNombre())));
            table.addCell(new Cell().add(new Paragraph(funcionario.getApellido())));
//            table.addCell(new Cell().add(new Paragraph(funcionario.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(funcionario.getTelefono())));
            table.addCell(new Cell().add(new Paragraph(funcionario.getEspecialidad() != null ? funcionario.getEspecialidad() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(funcionario.getGrado() != null ? funcionario.getGrado() : "N/A")));
//            table.addCell(new Cell().add(new Paragraph(funcionario.getActivo() == 1 ? "Sí" : "No")));
            table.addCell(new Cell().add(new Paragraph(funcionario.getCargo() != null ? funcionario.getCargo() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(funcionario.getRol() != null ? funcionario.getRol() : "N/A")));
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(disposition, "reporte_funcionarios.pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<byte[]> generarReporteFuncionarios() throws Exception {
        return generarReporte("inline");
    }

    @GetMapping("/funcionarios/descargar")
    public ResponseEntity<byte[]> descargarReporteFuncionarios() throws Exception {
        return generarReporte("attachment");
    }



    @GetMapping("/funcionarios/{funcionarioCedula}/misiones")
    public ResponseEntity<byte[]> generarReporteMisionesPorFuncionario(@PathVariable String funcionarioCedula) throws Exception {
        Optional<Funcionario> funcionarioOptional = funcionarioService.getFuncionarioByCedula(funcionarioCedula);
        if (!funcionarioOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Funcionario funcionario = funcionarioOptional.get();

        List<Mision> misiones = misionService.obtenerMisionesPorFuncionario(funcionario); // Usando el nuevo método


        // 3. Generar el PDF con la información del funcionario y sus misiones
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf);

        // Añadir información del funcionario al documento (opcional)
        document.add(new Paragraph("Reporte de Misiones del Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido()));
        document.add(new Paragraph(" ")); // Espacio

        // Crear la tabla para las misiones
        Table table = new Table(new float[]{2, 3, 2, 1}); // Ajusta el número y ancho de las columnas

        // Añadir encabezados de la tabla para las misiones
        table.addCell(new Cell().add(new Paragraph("Número Misión")));
        table.addCell(new Cell().add(new Paragraph("Actividades")));
        table.addCell(new Cell().add(new Paragraph("Caso")));
        table.addCell(new Cell().add(new Paragraph("Activo")));

        // Añadir datos de las misiones
        for (Mision mision : misiones) {
            table.addCell(new Cell().add(new Paragraph(mision.getNumeroMision().toString())));
            table.addCell(new Cell().add(new Paragraph(mision.getActividades() != null ? mision.getActividades() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(mision.getCaso() != null ? mision.getCaso().getCodigoCaso() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(mision.getActivo() ? "Sí" : "No")));
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_misiones_" + funcionario.getCedula() + ".pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/funcionarios/{funcionarioCedula}/misiones/descargar")
    public ResponseEntity<byte[]> descargarReporteMisionesPorFuncionario(@PathVariable String funcionarioCedula) throws Exception {
        Optional<Funcionario> funcionarioOptional = funcionarioService.getFuncionarioByCedula(funcionarioCedula);
        if (!funcionarioOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Funcionario funcionario = funcionarioOptional.get();

        List<Mision> misiones = misionService.obtenerMisionesPorFuncionario(funcionario);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf);

        document.add(new Paragraph("Reporte de Misiones del Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido()));
        document.add(new Paragraph(" "));

        Table table = new Table(new float[]{2, 3, 2, 1});
        table.addCell(new Cell().add(new Paragraph("Número Misión")));
        table.addCell(new Cell().add(new Paragraph("Actividades")));
        table.addCell(new Cell().add(new Paragraph("Caso")));
        table.addCell(new Cell().add(new Paragraph("Activo")));

        for (Mision mision : misiones) {
            table.addCell(new Cell().add(new Paragraph(mision.getNumeroMision().toString())));
            table.addCell(new Cell().add(new Paragraph(mision.getActividades() != null ? mision.getActividades() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(mision.getCaso() != null ? mision.getCaso().getCodigoCaso() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(mision.getActivo() ? "Sí" : "No")));
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporte_misiones_" + funcionario.getCedula() + ".pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/casos-por-funcionario/pdf")
    public ResponseEntity<byte[]> generarReporteCasosPorFuncionarioPdf() {
        Map<Funcionario, List<Caso>> datosReporte = reporteService.obtenerCasosPorFuncionario();
        byte[] pdfBytes = generarPdfCasosPorFuncionario(datosReporte); // Llama al método de generación de PDF

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_casos_por_funcionario.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private byte[] generarPdfCasosPorFuncionario(Map<Funcionario, List<Caso>> datos) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf);

        document.add(new Paragraph("Reporte de Casos Asignados por Funcionario"));
        document.add(new Paragraph(" "));

        for (Map.Entry<Funcionario, List<Caso>> entry : datos.entrySet()) {
            Funcionario funcionario = entry.getKey();
            List<Caso> casos = entry.getValue();

            document.add(new Paragraph("Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido() + " (Cédula: " + funcionario.getCedula() + ")"));
            document.add(new Paragraph(" "));

            if (!casos.isEmpty()) {
                Table table = new Table(new float[]{2, 5, 3}); // Ajusta el ancho de las columnas
                table.addCell(new Cell().add(new Paragraph("Código Caso")));
                table.addCell(new Cell().add(new Paragraph("Delito")));
                table.addCell(new Cell().add(new Paragraph("Estado (Activo)")));

                for (Caso caso : casos) {
                    table.addCell(new Cell().add(new Paragraph(caso.getCodigoCaso())));
                    table.addCell(new Cell().add(new Paragraph(caso.getDelito() != null ? caso.getDelito().getTipoDelito() : "N/A")));
                    table.addCell(new Cell().add(new Paragraph(caso.getActivo() ? "Sí" : "No")));
                }
                document.add(table);
                document.add(new Paragraph(" "));
            } else {
                document.add(new Paragraph("No tiene casos asignados."));
                document.add(new Paragraph(" "));
            }
        }

        document.close();
        return outputStream.toByteArray();
    }


}