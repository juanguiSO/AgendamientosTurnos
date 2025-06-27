package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.dto.FuncionarioDTO;
import com.agendamientos.agendamientosTurnos.entity.Caso;
import com.agendamientos.agendamientosTurnos.entity.Funcionario;
import com.agendamientos.agendamientosTurnos.entity.Mision;
import com.agendamientos.agendamientosTurnos.service.CasoService;
import com.agendamientos.agendamientosTurnos.service.FuncionarioService;
import com.agendamientos.agendamientosTurnos.service.MisionService;
import com.agendamientos.agendamientosTurnos.service.ReporteService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Generación de reportes en PDF para funcionarios")
public class ReportesController {

    private final FuncionarioService funcionarioService;
    private final MisionService misionService;
    private final ReporteService reporteService;
    private final CasoService casoService;

    @Autowired
    public ReportesController(FuncionarioService funcionarioService, MisionService misionService, ReporteService reporteService, CasoService casoService) {
        this.funcionarioService = funcionarioService;
        this.misionService = misionService;
        this.reporteService = reporteService;
        this.casoService = casoService;
    }

    private ResponseEntity<byte[]> generarReporte(String disposition) throws Exception {
        List<FuncionarioDTO> funcionarios = funcionarioService.getAllFuncionarios();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        // --- IMAGE ADDITION START ---
        String imagePath = "src/main/resources/imagenes/logo.jpg"; // Reemplazar con la ruta de tu imagen
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            // You can set properties for the image, e.g., scaling, positioning
            image.scaleToFit(100, 100); // Scale to a maximum width and height
            document.add(image);
            document.add(new Paragraph("\n")); // Add some spacing after the image
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        // --- IMAGE ADDITION END ---

        document.add(new Paragraph("Reporte de Funcionarios").setFont(customFont).setFontSize(20).setBold());
        document.add(new Paragraph("\n")); // Add some spacing

        Table table = new Table(new float[]{2, 2, 2, 2, 2, 1, 2, 2});

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        table.addCell(new Cell().add(new Paragraph("Cédula").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Nombre").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Apellido").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Teléfono").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Especialidad").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Grado").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Cargo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Rol").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

        boolean alternateColor = false;
        for (FuncionarioDTO funcionario : funcionarios) {
            Cell cedulaCell = new Cell().add(new Paragraph(funcionario.getCedula()).setFont(customFont));
            Cell nombreCell = new Cell().add(new Paragraph(funcionario.getNombre()).setFont(customFont));
            Cell apellidoCell = new Cell().add(new Paragraph(funcionario.getApellido()).setFont(customFont));
            Cell telefonoCell = new Cell().add(new Paragraph(funcionario.getTelefono()).setFont(customFont));
            Cell especialidadCell = new Cell().add(new Paragraph(funcionario.getEspecialidad() != null ? funcionario.getEspecialidad() : "N/A").setFont(customFont));
            Cell gradoCell = new Cell().add(new Paragraph(funcionario.getGrado() != null ? funcionario.getGrado() : "N/A").setFont(customFont));
            Cell cargoCell = new Cell().add(new Paragraph(funcionario.getCargo() != null ? funcionario.getCargo() : "N/A").setFont(customFont));
            Cell rolCell = new Cell().add(new Paragraph(funcionario.getRol() != null ? funcionario.getRol() : "N/A").setFont(customFont));

            if (alternateColor) {
                cedulaCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                nombreCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                apellidoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                telefonoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                especialidadCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                gradoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                cargoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                rolCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            }
            table.addCell(cedulaCell);
            table.addCell(nombreCell);
            table.addCell(apellidoCell);
            table.addCell(telefonoCell);
            table.addCell(especialidadCell);
            table.addCell(gradoCell);
            table.addCell(cargoCell);
            table.addCell(rolCell);
            alternateColor = !alternateColor;
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(disposition, "reporte_funcionarios.pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Generar vista previa del reporte de funcionarios en PDF", description = "Genera un reporte de funcionarios y lo devuelve como PDF en vista inline.")
    @GetMapping("/funcionarios")
    public ResponseEntity<byte[]> generarReporteFuncionarios() throws Exception {
        return generarReporte("inline");
    }

    @Operation(summary = "Descargar el reporte de funcionarios en PDF", description = "Genera y descarga un reporte en PDF con la información de todos los funcionarios.")
    @GetMapping("/funcionarios/descargar")
    public ResponseEntity<byte[]> descargarReporteFuncionarios() throws Exception {
        return generarReporte("attachment");
    }

    /**
     * Helper method to generate the PDF for missions by a specific employee.
     * This method is now unified to handle both inline viewing and attachment downloading.
     *
     * @param funcionarioCedula The ID number of the employee.
     * @param disposition The content disposition for the HTTP response ("inline" or "attachment").
     * @return byte array of the generated PDF.
     */
    private ResponseEntity<byte[]> generarPdfMisionesPorFuncionarioUnified(String funcionarioCedula, String disposition) throws Exception {
        Optional<Funcionario> funcionarioOptional = funcionarioService.getFuncionarioByCedula(funcionarioCedula);
        if (!funcionarioOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Funcionario funcionario = funcionarioOptional.get();

        List<Mision> misiones = misionService.obtenerMisionesPorFuncionario(funcionario);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg"; // Asegúrate que esta ruta sea correcta
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de misiones es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de misiones: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Misiones del Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido()).setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("\n"));

        Table table = new Table(new float[]{2, 3, 2, 1}); // Columnas: Número Misión, Actividades, Caso, Activo

        table.addCell(new Cell().add(new Paragraph("Número Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Actividades").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Caso").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Activo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

        boolean alternateColor = false;
        for (Mision mision : misiones) {
            table.addCell(new Cell().add(new Paragraph(mision.getNumeroMision() != null ? mision.getNumeroMision().toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph(mision.getActividades() != null ? mision.getActividades() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph(mision.getCaso() != null && mision.getCaso().getCodigoCaso() != null ? mision.getCaso().getCodigoCaso() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph(mision.getActivo() ? "Sí" : "No").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
            alternateColor = !alternateColor;
        }

        document.add(table);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(disposition, "reporte_misiones_" + funcionario.getCedula() + ".pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }


    /**
     * Genera un reporte PDF de misiones por funcionario.
     * El PDF se visualiza en línea en el navegador.
     */
    @Operation(summary = "Ver reporte PDF de misiones por funcionario",
            description = "Muestra en línea un reporte PDF con todas las misiones asignadas al funcionario con la cédula especificada.")
    @GetMapping("/funcionarios/{funcionarioCedula}/misiones")
    public ResponseEntity<byte[]> generarReporteMisionesPorFuncionarioOnline(@PathVariable String funcionarioCedula) throws Exception {
        return generarPdfMisionesPorFuncionarioUnified(funcionarioCedula, "inline");
    }

    /**
     * Genera y descarga un reporte PDF de misiones por funcionario.
     */
    @Operation(summary = "Descargar reporte PDF de misiones por funcionario",
            description = "Descarga un archivo PDF con todas las misiones asignadas al funcionario con la cédula especificada.")
    @GetMapping("/funcionarios/{funcionarioCedula}/misiones/descargar")
    public ResponseEntity<byte[]> descargarReporteMisionesPorFuncionarioOffline(@PathVariable String funcionarioCedula) throws Exception {
        return generarPdfMisionesPorFuncionarioUnified(funcionarioCedula, "attachment");
    }

    @Operation(summary = "Descargar reporte PDF de casos por funcionario",
            description = "Descarga un archivo PDF con los casos asignados a cada funcionario.")
    @GetMapping("/casos-por-funcionario/pdf/descargar")
    public ResponseEntity<byte[]> descargarReporteCasosPorFuncionarioPdf() {
        Map<Funcionario, List<Caso>> datosReporte = reporteService.obtenerCasosPorFuncionario();
        byte[] pdfBytes = generarPdfCasosPorFuncionario(datosReporte);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_casos_por_funcionario.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Ver reporte PDF de casos por funcionario",
            description = "Muestra en línea un reporte PDF con los casos asignados a cada funcionario.")
    @GetMapping("/casos-por-funcionario/pdf")
    public ResponseEntity<byte[]> generarReporteCasosPorFuncionarioPdf() {
        Map<Funcionario, List<Caso>> datosReporte = reporteService.obtenerCasosPorFuncionario();
        byte[] pdfBytes = generarPdfCasosPorFuncionario(datosReporte);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_casos_por_funcionario.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private byte[] generarPdfCasosPorFuncionario(Map<Funcionario, List<Caso>> datos) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont; // Declaración local
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // --- IMAGE ADDITION START for Casos por Funcionario Report ---
        String imagePath = "src/main/resources/imagenes/logo.jpg"; // Example image for cases report
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de casos es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de casos: " + e.getMessage());
        }
        // --- IMAGE ADDITION END ---

        document.add(new Paragraph("Reporte de Casos Asignados por Funcionario").setFont(customFont).setFontSize(20).setBold());
        document.add(new Paragraph(" ").setFont(customFont));

        for (Map.Entry<Funcionario, List<Caso>> entry : datos.entrySet()) {
            Funcionario funcionario = entry.getKey();
            List<Caso> casos = entry.getValue();

            document.add(new Paragraph("Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido() + " (Cédula: " + funcionario.getCedula() + ")").setFont(customFont).setFontSize(14).setBold());
            document.add(new Paragraph(" ").setFont(customFont));

            if (!casos.isEmpty()) {
                Table table = new Table(new float[]{2, 5, 3});

                table.addCell(new Cell().add(new Paragraph("Código Caso").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph("Delito").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph("Estado (Activo)").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

                boolean alternateColor = false;
                for (Caso caso : casos) {
                    Cell codigoCasoCell = new Cell().add(new Paragraph(caso.getCodigoCaso()).setFont(customFont));
                    Cell delitoCell = new Cell().add(new Paragraph(caso.getDelito() != null ? caso.getDelito().getTipoDelito() : "N/A").setFont(customFont));
                    Cell estadoCell = new Cell().add(new Paragraph(caso.getActivo() ? "Sí" : "No").setFont(customFont));

                    if (alternateColor) {
                        codigoCasoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                        delitoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                        estadoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                    }
                    table.addCell(codigoCasoCell);
                    table.addCell(delitoCell);
                    table.addCell(estadoCell);
                    alternateColor = !alternateColor;
                }
                document.add(table);
                document.add(new Paragraph(" ").setFont(customFont));
            } else {
                document.add(new Paragraph("No tiene casos asignados.").setFont(customFont));
                document.add(new Paragraph(" ").setFont(customFont));
            }
        }

        document.close();
        return outputStream.toByteArray();
    }

    @Operation(
            summary = "Visualizar reporte PDF de viajes por estado",
            description = "Genera un reporte PDF que muestra todos los viajes filtrados por un estado específico. El PDF se muestra en línea en el navegador."
    )
    @GetMapping("/viajes/estado/{idEstadoViaje}/pdf")
    public ResponseEntity<byte[]> visualizarReporteViajesPorEstado(@PathVariable Integer idEstadoViaje) {
        return generarReporteViajesPorEstadoPdf(idEstadoViaje, true);
    }

    @Operation(
            summary = "Descargar reporte PDF de viajes por estado",
            description = "Descarga un archivo PDF que contiene los viajes filtrados por un estado específico."
    )
    @GetMapping("/viajes/estado/{idEstadoViaje}/descargar")
    public ResponseEntity<byte[]> descargarReporteViajesPorEstado(@PathVariable Integer idEstadoViaje) {
        return generarReporteViajesPorEstadoPdf(idEstadoViaje, false);
    }

    private ResponseEntity<byte[]> generarReporteViajesPorEstadoPdf(Integer idEstadoViaje, boolean enLinea) {
        try {
            byte[] pdfBytes = reporteService.generarReporteViajesPorEstado(idEstadoViaje);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String disposition = enLinea ? "inline" : "attachment";
            headers.setContentDispositionFormData(disposition, "reporte_viajes_estado_" + idEstadoViaje + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error al generar el reporte de viajes: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Genera un reporte PDF de misiones finalizadas para un funcionario en un rango de fechas.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato yyyy-MM-dd HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato yyyy-MM-dd HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descargar reporte PDF de misiones finalizadas por funcionario",
            description = "Descarga un archivo PDF que contiene las misiones finalizadas de un funcionario en un tango de fechas, se debe ingresar la cédula."
    )
    @GetMapping("/misiones-finalizadas-por-funcionario-y-rango/{funcionarioCedula}/pdf")
    public ResponseEntity<byte[]> generarReporteMisionesFinalizadasPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr); // Asume formato ISO_LOCAL_DATE_TIME
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr); // Asume formato ISO_LOCAL_DATE_TIME
            }

            // Llamar al servicio para obtener los datos de las misiones finalizadas
            // Asegúrate de que el ReporteService tenga este método
            List<Object[]> misionesFinalizadas = reporteService.obtenerMisionesFinalizadasPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            // Generar el PDF con los datos obtenidos
            byte[] pdfBytes = generarPdfMisionesFinalizadas(misionesFinalizadas, funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_misiones_finalizadas_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use yyyy-MM-ddTHH:mm:ss o yyyy-MM-dd HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de misiones finalizadas: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un reporte PDF de misiones finalizadas para un funcionario en un rango de fechas.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato yyyy-MM-dd HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato yyyy-MM-dd HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descargar PDF de misiones finalizadas por funcionario en un rango de fechas",
            description = "Genera y descarga un reporte en formato PDF con las misiones finalizadas asociadas a un funcionario específico, filtradas por un rango de fechas."
    )
    @GetMapping("/misiones-finalizadas-por-funcionario-y-rango/{funcionarioCedula}/pdf/descargar")
    public ResponseEntity<byte[]> descargarReporteMisionesFinalizadasPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr); // Asume formato ISO_LOCAL_DATE_TIME
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr); // Asume formato ISO_LOCAL_DATE_TIME
            }

            // Llamar al servicio para obtener los datos de las misiones finalizadas
            // Asegúrate de que el ReporteService tenga este método
            List<Object[]> misionesFinalizadas = reporteService.obtenerMisionesFinalizadasPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            // Generar el PDF con los datos obtenidos
            byte[] pdfBytes = generarPdfMisionesFinalizadas(misionesFinalizadas, funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_misiones_finalizadas_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use yyyy-MM-ddTHH:mm:ss o yyyy-MM-dd HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de misiones finalizadas: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper method to generate the PDF for finalized missions.
     * @param misionesData List of Object arrays with mission data (from the custom SQL query).
     * @param cedulaFuncionario The ID number of the employee.
     * @param fechaInicio The start date for the report.
     * @param fechaFin The end date for the report.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfMisionesFinalizadas(List<Object[]> misionesData, String cedulaFuncionario, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de misiones finalizadas es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de misiones finalizadas: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Misiones Finalizadas por Funcionario y Rango de Fechas").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("Funcionario Cédula: " + cedulaFuncionario).setFont(customFont).setFontSize(12));
        String rangoFechas = (fechaInicio != null ? fechaInicio.toString() : "N/A") + " - " + (fechaFin != null ? fechaFin.toString() : "N/A");
        document.add(new Paragraph("Rango de Fechas: " + rangoFechas).setFont(customFont).setFontSize(12));
        document.add(new Paragraph("\n"));

        if (misionesData.isEmpty()) {
            document.add(new Paragraph("No se encontraron misiones finalizadas para el funcionario y rango de fechas especificados.").setFont(customFont));
        } else {
            // Define table columns based on your SQL query's selected columns
            // f.nombre, f.apellido, f.cedula, m.numero_mision, v.id_viaje, v.tiempo_inicio, v.tiempo_fin, ev.estado_viaje
            Table table = new Table(new float[]{1.5f, 1.5f, 2, 1.5f, 2.5f, 2.5f, 1.5f}); // Adjust column widths as needed

            table.addCell(new Cell().add(new Paragraph("Nombre Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Apellido Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Cédula Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Num. Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
           // table.addCell(new Cell().add(new Paragraph("ID Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Inicio Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Fin Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Estado Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Object[] row : misionesData) {
                // Cast to String for display, assuming all are strings from the query output
                table.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[4] != null ? row[4].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[5] != null ? row[5].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[6] != null ? row[6].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
               // table.addCell(new Cell().add(new Paragraph(row[7] != null ? row[7].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(table);
        }

        document.add(new Paragraph("\n")); // Espacio
        document.add(new Paragraph("Total de Misiones Finalizadas: " + misionesData.size())
                .setFont(customFont).setFontSize(14).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));


        document.close();
        return outputStream.toByteArray();
    }

    /**
     * Genera un reporte PDF de viajes Reprogramados o Cancelados para un funcionario en un rango de fechas.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Visualizar PDF de viajes reprogramados o cancelados por funcionario en un rango de fechas",
            description = "Genera un reporte en PDF con los viajes reprogramados o cancelados asociados a un funcionario específico, filtrados por un rango de fechas. El PDF se muestra en línea en el navegador."
    )
    @GetMapping("/viajes-reprogramados-cancelados-por-funcionario-y-rango/{funcionarioCedula}/pdf")
    public ResponseEntity<byte[]> generarReporteViajesReprogramadosOCanceladosPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            List<Object[]> viajesReporte = reporteService.obtenerViajesReprogramadosOCanceladosPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesReprogramadosOCancelados(viajesReporte, funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_viajes_reprogramados_cancelados_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes reprogramados/cancelados: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un reporte PDF de viajes Reprogramados o Cancelados para un funcionario en un rango de fechas.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descargar PDF de viajes reprogramados o cancelados por funcionario en un rango de fechas",
            description = "Genera y descarga un reporte en PDF con los viajes reprogramados o cancelados de un funcionario específico, filtrados por un rango de fechas. El archivo se descarga como attachment."
    )
    @GetMapping("/viajes-reprogramados-cancelados-por-funcionario-y-rango/{funcionarioCedula}/pdf/descargar")
    public ResponseEntity<byte[]> descargarReporteViajesReprogramadosOCanceladosPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            List<Object[]> viajesReporte = reporteService.obtenerViajesReprogramadosOCanceladosPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesReprogramadosOCancelados(viajesReporte, funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_viajes_reprogramados_cancelados_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use Букмекерлар-MM-ddTHH:mm:ss o Букмекерлар-MM-dd HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes reprogramados/cancelados: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper method to generate the PDF for reprogrammed or canceled trips.
     * Adds the 'Observación' column.
     * @param viajesData List of Object arrays with trip data (from the custom SQL query).
     * @param cedulaFuncionario The ID number of the employee.
     * @param fechaInicio The start date for the report.
     * @param fechaFin The end date for the report.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfViajesReprogramadosOCancelados(List<Object[]> viajesData, String cedulaFuncionario, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de viajes reprogramados/cancelados es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de viajes reprogramados/cancelados: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Viajes Reprogramados o Cancelados por Funcionario y Rango de Fechas").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("Funcionario Cédula: " + cedulaFuncionario).setFont(customFont).setFontSize(12));
        String rangoFechas = (fechaInicio != null ? fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A") + " - " + (fechaFin != null ? fechaFin.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
        document.add(new Paragraph("Rango de Fechas: " + rangoFechas).setFont(customFont).setFontSize(12));
        document.add(new Paragraph("\n"));

        if (viajesData.isEmpty()) {
            document.add(new Paragraph("No se encontraron viajes reprogramados o cancelados para el funcionario y rango de fechas especificados.").setFont(customFont));
        } else {
            // Se añaden 9 columnas: 8 originales + 1 para Observación
            // f.nombre, f.apellido, f.cedula, m.numero_mision, v.id_viaje, v.tiempo_inicio, v.tiempo_fin, ev.estado_viaje, v.observacion
            Table table = new Table(new float[]{1.2f, 1.2f, 1.8f, 1.2f,2.2f, 2.2f, 1.2f, 3f}); // Ajusta anchos de columna si es necesario

            table.addCell(new Cell().add(new Paragraph("Nom. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Apel. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Cédula Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Num. Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            //table.addCell(new Cell().add(new Paragraph("ID Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Inicio Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Fin Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Estado Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Observación").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE)); // ¡Nueva cabecera!

            boolean alternateColor = false;
            for (Object[] row : viajesData) {
                // Asegúrate de que los índices coincidan con el orden de tu SELECT en el servicio
                // f.nombre (0), f.apellido (1), f.cedula (2), m.numero_mision (3), v.id_viaje (4),
                // v.tiempo_inicio (5), v.tiempo_fin (6), ev.estado_viaje (7), v.observacion (8)
                table.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[4] != null ? row[4].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[5] != null ? row[5].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[6] != null ? row[6].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[7] != null ? row[7].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
               // table.addCell(new Cell().add(new Paragraph(row[8] != null ? row[8].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE)); // ¡Nueva celda!
                alternateColor = !alternateColor;
            }
            document.add(table);
        }
        document.add(new Paragraph("\n")); // Espacio
        document.add(new Paragraph("Total de Viajes Reprogramados/Cancelados: " + viajesData.size())
                .setFont(customFont).setFontSize(14).setBold().setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));

        document.close();
        return outputStream.toByteArray();
    }


    /**
     * Genera un reporte PDF de viajes realizados por un funcionario en un rango de fechas,
     * diferenciando entre viajes con y sin viáticos, y totalizando cada categoría.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Visualizar PDF de viajes con y sin viáticos por funcionario y rango de fechas",
            description = "Genera un reporte en PDF diferenciando los viajes con y sin viáticos de un funcionario específico. "
                    + "El PDF se genera en línea en el navegador. Las fechas deben estar en formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reporte generado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "400", description = "Error en el formato de fecha o parámetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/viajes-con-sin-viaticos-por-funcionario-y-rango/{funcionarioCedula}/pdf")
    public ResponseEntity<byte[]> generarReporteViajesConSinViaticosPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            // El servicio devolverá un Map con dos listas: "conViaticos" y "sinViaticos"
            Map<String, List<Object[]>> viajesPorCategoria = reporteService.obtenerViajesConSinViaticosPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesConSinViaticos(
                    viajesPorCategoria.getOrDefault("conViaticos", List.of()),
                    viajesPorCategoria.getOrDefault("sinViaticos", List.of()),
                    funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_viajes_viaticos_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes con/sin viáticos: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Genera un reporte PDF de viajes realizados por un funcionario en un rango de fechas,
     * diferenciando entre viajes con y sin viáticos, y totalizando cada categoría.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicioStr La fecha de inicio del rango (formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss).
     * @param fechaFinStr La fecha de fin del rango (formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descargar PDF de viajes con y sin viáticos por funcionario y rango de fechas",
            description = "Genera y descarga un reporte en PDF diferenciando los viajes con y sin viáticos de un funcionario específico. "
                    + "Las fechas deben estar en formato YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reporte generado y descargado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "400", description = "Error en el formato de fecha o parámetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/viajes-con-sin-viaticos-por-funcionario-y-rango/{funcionarioCedula}/pdf/descargar")
    public ResponseEntity<byte[]> descargarReporteViajesConSinViaticosPorFuncionarioYRango(
            @PathVariable String funcionarioCedula,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            // El servicio devolverá un Map con dos listas: "conViaticos" y "sinViaticos"
            Map<String, List<Object[]>> viajesPorCategoria = reporteService.obtenerViajesConSinViaticosPorFuncionarioYRango(
                    funcionarioCedula, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesConSinViaticos(
                    viajesPorCategoria.getOrDefault("conViaticos", List.of()),
                    viajesPorCategoria.getOrDefault("sinViaticos", List.of()),
                    funcionarioCedula, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_viajes_viaticos_" + funcionarioCedula + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use YYYY-MM-DDTHH:mm:ss o YYYY-MM-DD HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes con/sin viáticos: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Helper method to generate the PDF for trips with and without per diem (viaticos).
     * Displays separate tables and totals for each category.
     * @param viajesConViaticos List of Object arrays for trips with per diem.
     * @param viajesSinViaticos List of Object arrays for trips without per diem.
     * @param cedulaFuncionario The ID number of the employee.
     * @param fechaInicio The start date for the report.
     * @param fechaFin The end date for the report.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfViajesConSinViaticos(List<Object[]> viajesConViaticos, List<Object[]> viajesSinViaticos,
                                                  String cedulaFuncionario, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate());

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de viajes con/sin viáticos es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de viajes con/sin viáticos: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Viajes con y sin Viáticos por Funcionario y Rango de Fechas").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("Funcionario Cédula: " + cedulaFuncionario).setFont(customFont).setFontSize(12));
        String rangoFechas = (fechaInicio != null ? fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A") + " - " + (fechaFin != null ? fechaFin.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
        document.add(new Paragraph("Rango de Fechas: " + rangoFechas).setFont(customFont).setFontSize(12));
        document.add(new Paragraph("\n"));

        // ----- Sección de Viajes CON Viáticos -----
        document.add(new Paragraph("VIAJES CON VIÁTICOS").setFont(customFont).setFontSize(16).setBold().setUnderline());
        document.add(new Paragraph("\n"));

        if (viajesConViaticos.isEmpty()) {
            document.add(new Paragraph("No se encontraron viajes con viáticos para el funcionario y rango de fechas especificados.").setFont(customFont));
        } else {
            // Columnas: f.nombre, f.apellido, f.cedula, m.numero_mision, v.tiempo_inicio, v.tiempo_fin, ev.estado_viaje
            Table tableConViaticos = new Table(new float[]{1.5f, 1.5f, 2, 1.5f, 2.5f, 2.5f, 1.5f});

            tableConViaticos.addCell(new Cell().add(new Paragraph("Nom. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Apel. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Cédula Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Num. Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Inicio Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Fin Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableConViaticos.addCell(new Cell().add(new Paragraph("Estado Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Object[] row : viajesConViaticos) {
                // Índices: f.nombre (0), f.apellido (1), f.cedula (2), m.numero_mision (3),
                // v.tiempo_inicio (4), v.tiempo_fin (5), ev.estado_viaje (6)
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[4] != null ? row[4].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[5] != null ? row[5].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableConViaticos.addCell(new Cell().add(new Paragraph(row[6] != null ? row[6].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(tableConViaticos);

            // Añadir el total de viajes CON viáticos
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de Viajes con Viáticos: " + viajesConViaticos.size())
                    .setFont(customFont).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));
        }

        // ----- Sección de Viajes SIN Viáticos -----
        document.add(new Paragraph("\n\n")); // Espacio entre secciones
        document.add(new Paragraph("VIAJES SIN VIÁTICOS").setFont(customFont).setFontSize(16).setBold().setUnderline());
        document.add(new Paragraph("\n"));

        if (viajesSinViaticos.isEmpty()) {
            document.add(new Paragraph("No se encontraron viajes sin viáticos para el funcionario y rango de fechas especificados.").setFont(customFont));
        } else {
            // Columnas: f.nombre, f.apellido, f.cedula, m.numero_mision, v.tiempo_inicio, v.tiempo_fin, ev.estado_viaje
            Table tableSinViaticos = new Table(new float[]{1.5f, 1.5f, 2, 1.5f, 2.5f, 2.5f, 1.5f});

            tableSinViaticos.addCell(new Cell().add(new Paragraph("Nom. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Apel. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Cédula Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Num. Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Inicio Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Fin Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            tableSinViaticos.addCell(new Cell().add(new Paragraph("Estado Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Object[] row : viajesSinViaticos) {
                // Índices: f.nombre (0), f.apellido (1), f.cedula (2), m.numero_mision (3),
                // v.tiempo_inicio (4), v.tiempo_fin (5), ev.estado_viaje (6)
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[4] != null ? row[4].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[5] != null ? row[5].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                tableSinViaticos.addCell(new Cell().add(new Paragraph(row[6] != null ? row[6].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(tableSinViaticos);

            // Añadir el total de viajes SIN viáticos
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total de Viajes sin Viáticos: " + viajesSinViaticos.size())
                    .setFont(customFont).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));
        }

        document.close();
        return outputStream.toByteArray();
    }
    /**
     * Genera un reporte PDF con todas las misiones actualmente activas.
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Visualiza PDF de misiones activas",
            description = "Genera un reporte en PDF con todas las misiones que se encuentran actualmente activas. El PDF se muestra en el navegador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF generado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/misiones-activas/pdf")
    public ResponseEntity<byte[]> generarReporteMisionesActivas() {
        try {
            List<Object[]> misionesActivas = reporteService.obtenerMisionesActivas();
            byte[] pdfBytes = generarPdfMisionesActivas(misionesActivas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_misiones_activas.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de misiones activas: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un reporte PDF con todas las misiones actualmente activas.
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descarga PDF de misiones activas",
            description = "Genera un archivo PDF descargable con todas las misiones que se encuentran actualmente activas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF generado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/misiones-activas/pdf/descagar")
    public ResponseEntity<byte[]> descargarReporteMisionesActivas() {
        try {
            List<Object[]> misionesActivas = reporteService.obtenerMisionesActivas();
            byte[] pdfBytes = generarPdfMisionesActivas(misionesActivas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_misiones_activas.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de misiones activas: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Helper method to generate the PDF for active missions.
     * Uses 'activo' field from 'mision' table.
     * @param misionesData List of Object arrays with active mission data.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfMisionesActivas(List<Object[]> misionesData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4); // A4 normal, no rotado

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de misiones activas es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de misiones activas: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Misiones Activas").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("Fecha de Generación: " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).setFont(customFont).setFontSize(10));
        document.add(new Paragraph("\n"));

        if (misionesData.isEmpty()) {
            document.add(new Paragraph("No se encontraron misiones activas en el sistema.").setFont(customFont));
        } else {
            // Columnas ahora son 4: m.numero_mision, f.nombre, f.apellido, f.cedula
            // El array de floats debe tener 4 elementos, uno por cada columna
            Table table = new Table(new float[]{1.5f, 2.5f, 2.5f, 2.5f});

            table.addCell(new Cell().add(new Paragraph("Num. Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Nom. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Apel. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Cédula Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            // ELIMINADO: Celda para "Descripción Misión" porque ya no se selecciona

            boolean alternateColor = false;
            for (Object[] row : misionesData) {
                // Índices: m.numero_mision (0), f.nombre (1), f.apellido (2), f.cedula (3)
                // Se eliminó la referencia a row[4] que causaba el error
                table.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(table);

            // Añadir el total al final
            document.add(new Paragraph("\n")); // Espacio
            document.add(new Paragraph("Total de Misiones Activas: " + misionesData.size())
                    .setFont(customFont).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));
        }

        document.close();
        return outputStream.toByteArray();
    }
    /**
     * Genera un reporte PDF de viajes finalizados por un vehículo específico en un rango de fechas.
     * @param idVehiculo El ID del vehículo.
     * @param fechaInicioStr La fecha de inicio del rango (formato ISO 8601).
     * @param fechaFinStr La fecha de fin del rango (formato ISO 8601).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Visualiza PDF de viajes finalizados por vehículo",
            description = "Genera un reporte PDF con los viajes finalizados de un vehículo específico en un rango de fechas. El PDF se muestra en el navegador.",
            parameters = {
                    @Parameter(name = "idVehiculo", description = "ID del vehículo", required = true),
                    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio del rango (formato ISO 8601)", required = false),
                    @Parameter(name = "fechaFinStr", description = "Fecha de fin del rango (formato ISO 8601)", required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF generado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "400", description = "Error en el formato de fecha o parámetros"),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/viajes-finalizados-por-vehiculo/{idVehiculo}/pdf")
    public ResponseEntity<byte[]> generarReporteViajesFinalizadosPorVehiculo(
            @PathVariable Integer idVehiculo,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            List<Object[]> viajesFinalizados = reporteService.obtenerViajesFinalizadosPorVehiculoYRango(
                    idVehiculo, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesFinalizadosPorVehiculo(
                    viajesFinalizados, idVehiculo, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_viajes_vehiculo_" + idVehiculo + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use yyyy-MM-DDTHH:mm:ss o yyyy-MM-DD HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes finalizados por vehículo: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un reporte PDF de viajes finalizados por un vehículo específico en un rango de fechas.
     * @param idVehiculo El ID del vehículo.
     * @param fechaInicioStr La fecha de inicio del rango (formato ISO 8601).
     * @param fechaFinStr La fecha de fin del rango (formato ISO 8601).
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descarga PDF de viajes finalizados por vehículo",
            description = "Genera un archivo PDF descargable con los viajes finalizados de un vehículo específico en un rango de fechas.",
            parameters = {
                    @Parameter(name = "idVehiculo", description = "ID del vehículo", required = true),
                    @Parameter(name = "fechaInicioStr", description = "Fecha de inicio del rango (formato ISO 8601)", required = false),
                    @Parameter(name = "fechaFinStr", description = "Fecha de fin del rango (formato ISO 8601)", required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF descargado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "400", description = "Error en el formato de fecha o parámetros"),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/viajes-finalizados-por-vehiculo/{idVehiculo}/pdf/descagar")
    public ResponseEntity<byte[]> descargarReporteViajesFinalizadosPorVehiculo(
            @PathVariable Integer idVehiculo,
            @RequestParam(required = false) String fechaInicioStr,
            @RequestParam(required = false) String fechaFinStr) {
        try {
            LocalDateTime fechaInicio = null;
            LocalDateTime fechaFin = null;

            if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                fechaInicio = LocalDateTime.parse(fechaInicioStr);
            }
            if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                fechaFin = LocalDateTime.parse(fechaFinStr);
            }

            List<Object[]> viajesFinalizados = reporteService.obtenerViajesFinalizadosPorVehiculoYRango(
                    idVehiculo, fechaInicio, fechaFin);

            byte[] pdfBytes = generarPdfViajesFinalizadosPorVehiculo(
                    viajesFinalizados, idVehiculo, fechaInicio, fechaFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_viajes_vehiculo_" + idVehiculo + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            String errorMessage = "Error en el formato de fecha. Use yyyy-MM-DDTHH:mm:ss o yyyy-MM-DD HH:mm:ss. Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Error en los parámetros: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de viajes finalizados por vehículo: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper method to generate the PDF for finalized trips by a specific vehicle.
     * @param viajesData List of Object arrays with finalized trip data.
     * @param idVehiculo The ID of the vehicle.
     * @param fechaInicio The start date for the report.
     * @param fechaFin The end date for the report.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfViajesFinalizadosPorVehiculo(List<Object[]> viajesData, Integer idVehiculo, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4.rotate()); // Rotamos para más columnas

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de viajes finalizados por vehículo es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de viajes finalizados por vehículo: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Viajes Finalizados por Vehículo").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("ID Vehículo: " + idVehiculo).setFont(customFont).setFontSize(12));
        String rangoFechas = (fechaInicio != null ? fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A") + " - " + (fechaFin != null ? fechaFin.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A");
        document.add(new Paragraph("Rango de Fechas: " + rangoFechas).setFont(customFont).setFontSize(12));
        document.add(new Paragraph("\n"));

        if (viajesData.isEmpty()) {
            document.add(new Paragraph("No se encontraron viajes finalizados para el vehículo y rango de fechas especificados.").setFont(customFont));
        } else {
            // Columnas: id_viaje (0), marca (1), modelo (2), placa (3), tiempo_inicio (4), tiempo_fin (5),
            // estado_actual_viaje (6), distancia_recorrida (7), observacion (8), viatico (9), nombre_funcionario (10), apellido_funcionario (11)
            Table table = new Table(new float[]{0.8f, 1.2f, 1.2f, 1.2f, 2.2f, 2.2f, 1.2f, 1.2f, 2.5f, 0.8f, 1.5f, 1.5f});

            table.addCell(new Cell().add(new Paragraph("ID Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Marca").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Modelo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Placa").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Inicio Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Fin Viaje").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Estado").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Dist. (Km)").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Observación").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Viático").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Nom. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Apel. Func.").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Object[] row : viajesData) {
                // Índices: id_viaje (0), marca (1), modelo (2), placa (3), tiempo_inicio (4), tiempo_fin (5),
                // estado_actual_viaje (6), distancia_recorrida (7), observacion (8), viatico (9),
                // nombre_funcionario (10), apellido_funcionario (11)
                table.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[4] != null ? row[4].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[5] != null ? row[5].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[6] != null ? row[6].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[7] != null ? row[7].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[8] != null ? row[8].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));

                // Conversión de TINYINT(1) a String "Sí"/"No" para viáticos
                String viaticoStatus = "N/A";
                if (row[9] != null) {
                    if (row[9] instanceof Boolean) {
                        viaticoStatus = (Boolean) row[9] ? "Sí" : "No";
                    } else if (row[9] instanceof Number) { // Común para TINYINT(1)
                        viaticoStatus = (((Number) row[9]).intValue() == 1) ? "Sí" : "No";
                    }
                }
                table.addCell(new Cell().add(new Paragraph(viaticoStatus).setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));

                table.addCell(new Cell().add(new Paragraph(row[10] != null ? row[10].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[11] != null ? row[11].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(table);

            // Añadir el total al final
            document.add(new Paragraph("\n")); // Espacio
            document.add(new Paragraph("Total de Viajes Finalizados para este Vehículo: " + viajesData.size())
                    .setFont(customFont).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));
        }

        document.close();
        return outputStream.toByteArray();
    }
    /**
     * Genera un reporte PDF con la lista de todos los vehículos y su estado actual.
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Visualiza PDF de vehículos por estado",
            description = "Genera un reporte PDF con todos los vehículos y su estado actual. El reporte se abre en el navegador.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF generado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/vehiculos-por-estado/pdf")
    public ResponseEntity<byte[]> generarReporteVehiculosPorEstado() {
        try {
            List<Object[]> vehiculosConEstado = reporteService.obtenerVehiculosConEstado();
            byte[] pdfBytes = generarPdfVehiculosConEstado(vehiculosConEstado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "reporte_vehiculos_estado.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de vehículos por estado: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un reporte PDF con la lista de todos los vehículos y su estado actual.
     * @return ResponseEntity con el PDF en bytes o un error.
     */
    @Operation(
            summary = "Descarga PDF de vehículos por estado",
            description = "Genera un archivo PDF descargable con la lista de todos los vehículos y su estado actual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF descargado correctamente", content = @Content(mediaType = "application/pdf")),
                    @ApiResponse(responseCode = "500", description = "Error interno al generar el reporte")
            }
    )
    @GetMapping("/vehiculos-por-estado/pdf/descagar")
    public ResponseEntity<byte[]> descargarReporteVehiculosPorEstado() {
        try {
            List<Object[]> vehiculosConEstado = reporteService.obtenerVehiculosConEstado();
            byte[] pdfBytes = generarPdfVehiculosConEstado(vehiculosConEstado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_vehiculos_estado.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Error interno al generar el reporte de vehículos por estado: " + e.getMessage();
            return new ResponseEntity<>(errorMessage.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Helper method to generate the PDF for vehicles with their current status.
     * @param vehiculosData List of Object arrays with vehicle data and status.
     * @return byte array of the generated PDF.
     */
    private byte[] generarPdfVehiculosConEstado(List<Object[]> vehiculosData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.kernel.pdf.PdfDocument pdf = new com.itextpdf.kernel.pdf.PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdf, PageSize.A4); // A4 normal, debería ser suficiente

        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont("src/main/resources/fonts/verdana.ttf", EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente Verdana: " + e.getMessage());
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("FATAL ERROR: No se pudo cargar ni la fuente personalizada ni la fuente de respaldo Helvetica: " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }

        DeviceRgb customHeaderColor = new DeviceRgb(30, 41, 59);

        // Add Image
        String imagePath = "src/main/resources/imagenes/logo.jpg";
        try {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80);
            document.add(image);
            document.add(new Paragraph("\n"));
        } catch (MalformedURLException e) {
            System.err.println("Error: La URL de la imagen para el reporte de vehículos es incorrecta o el archivo no existe. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen para el reporte de vehículos: " + e.getMessage());
        }

        document.add(new Paragraph("Reporte de Vehículos por Estado").setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph("Fecha de Generación: " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).setFont(customFont).setFontSize(10));
        document.add(new Paragraph("\n"));

        if (vehiculosData.isEmpty()) {
            document.add(new Paragraph("No se encontraron vehículos en el sistema.").setFont(customFont));
        } else {
            // Columnas: marca (0), modelo (1), placa (2), estado_vehiculo (3)
            Table table = new Table(new float[]{2f, 2f, 2f, 2f}); // 4 columnas

            table.addCell(new Cell().add(new Paragraph("Marca").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Modelo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Placa").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
            table.addCell(new Cell().add(new Paragraph("Estado").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

            boolean alternateColor = false;
            for (Object[] row : vehiculosData) {
                // Índices: marca (0), modelo (1), placa (2), estado_vehiculo (3)
                table.addCell(new Cell().add(new Paragraph(row[0] != null ? row[0].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[1] != null ? row[1].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[2] != null ? row[2].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                table.addCell(new Cell().add(new Paragraph(row[3] != null ? row[3].toString() : "N/A").setFont(customFont)).setBackgroundColor(alternateColor ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE));
                alternateColor = !alternateColor;
            }
            document.add(table);

            // Añadir el total al final
            document.add(new Paragraph("\n")); // Espacio
            document.add(new Paragraph("Total de Vehículos: " + vehiculosData.size())
                    .setFont(customFont).setFontSize(14).setBold().setTextAlignment(TextAlignment.RIGHT));
        }

        document.close();
        return outputStream.toByteArray();
    }
}