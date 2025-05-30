package com.agendamientos.agendamientosTurnos.controller;

import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.service.CasoService;
import com.agendamientos.agendamientosTurnos.service.ReporteService;
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
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
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

        List<Mision> misiones = misionService.obtenerMisionesPorFuncionario(funcionario);

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

        // --- IMAGE ADDITION START for Misiones Report ---
        String imagePath = "src/main/resources/imagenes/logo.jpg"; // Example image for missions report
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
        // --- IMAGE ADDITION END ---

        document.add(new Paragraph("Reporte de Misiones del Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido()).setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph(" ").setFont(customFont));

        Table table = new Table(new float[]{2, 3, 2, 1});

        table.addCell(new Cell().add(new Paragraph("Número Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Actividades").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Caso").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Activo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

        boolean alternateColor = false;
        for (Mision mision : misiones) {
            Cell numeroMisionCell = new Cell().add(new Paragraph(mision.getNumeroMision().toString()).setFont(customFont));
            Cell actividadesCell = new Cell().add(new Paragraph(mision.getActividades() != null ? mision.getActividades() : "N/A").setFont(customFont));
            Cell casoCell = new Cell().add(new Paragraph(mision.getCaso() != null ? mision.getCaso().getCodigoCaso() : "N/A").setFont(customFont));
            Cell activoCell = new Cell().add(new Paragraph(mision.getActivo() ? "Sí" : "No").setFont(customFont));

            if (alternateColor) {
                numeroMisionCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                actividadesCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                casoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                activoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            }
            table.addCell(numeroMisionCell);
            table.addCell(actividadesCell);
            table.addCell(casoCell);
            table.addCell(activoCell);
            alternateColor = !alternateColor;
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

        // --- IMAGE ADDITION START for Misiones Report (Download) ---
        String imagePath = "src/main/resources/images/imagenes/logo.jpg"; // Example image for missions report
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
        // --- IMAGE ADDITION END ---

        document.add(new Paragraph("Reporte de Misiones del Funcionario: " + funcionario.getNombre() + " " + funcionario.getApellido()).setFont(customFont).setFontSize(18).setBold());
        document.add(new Paragraph(" ").setFont(customFont));

        Table table = new Table(new float[]{2, 3, 2, 1});
        table.addCell(new Cell().add(new Paragraph("Número Misión").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Actividades").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Caso").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));
        table.addCell(new Cell().add(new Paragraph("Activo").setFont(customFont)).setBackgroundColor(customHeaderColor).setFontColor(ColorConstants.WHITE));

        boolean alternateColor = false;
        for (Mision mision : misiones) {
            Cell numeroMisionCell = new Cell().add(new Paragraph(mision.getNumeroMision().toString()).setFont(customFont));
            Cell actividadesCell = new Cell().add(new Paragraph(mision.getActividades() != null ? mision.getActividades() : "N/A").setFont(customFont));
            Cell casoCell = new Cell().add(new Paragraph(mision.getCaso() != null ? mision.getCaso().getCodigoCaso() : "N/A").setFont(customFont));
            Cell activoCell = new Cell().add(new Paragraph(mision.getActivo() ? "Sí" : "No").setFont(customFont));

            if (alternateColor) {
                numeroMisionCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                actividadesCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                casoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                activoCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            }
            table.addCell(numeroMisionCell);
            table.addCell(actividadesCell);
            table.addCell(casoCell);
            table.addCell(activoCell);
            alternateColor = !alternateColor;
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
}