package com.agendamientos.agendamientosTurnos.util; // <--- Asegúrate de que este sea el paquete correcto

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

// No es necesario que sea un @Component si todos sus métodos son estáticos
// Si necesitas inyectarlo en el futuro, puedes añadir @Component
public class PdfGeneratorUtil {

    private static final String FONT_PATH = "src/main/resources/fonts/verdana.ttf";
    private static final String LOGO_PATH = "src/main/resources/imagenes/logo.jpg";
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(30, 41, 59);

    // Método para inicializar un documento PDF y añadir encabezado/imagen
    public static Document initializePdfDocument(ByteArrayOutputStream baos, boolean rotatePage) throws IOException {
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, rotatePage ? PageSize.A4.rotate() : PageSize.A4);

        // Añadir márgenes si es necesario
        document.setMargins(50, 50, 50, 50);

        // Intenta añadir la imagen del logo
        try {
            ImageData imageData = ImageDataFactory.create(LOGO_PATH);
            Image image = new Image(imageData);
            image.scaleToFit(80, 80); // Ajusta tamaño
            // image.setFixedPosition(pdf.getDefaultPageSize().getWidth() - 100, pdf.getDefaultPageSize().getHeight() - 100, 80); // Puedes usar esto para posicionar fijo
            document.add(image);
            document.add(new Paragraph("\n")); // Espacio después de la imagen
        } catch (MalformedURLException e) {
            System.err.println("Advertencia: La URL de la imagen del logo es incorrecta. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Advertencia: Error al cargar la imagen del logo. " + e.getMessage());
        }
        return document;
    }

    // Método para obtener la fuente personalizada o de respaldo
    public static PdfFont getCustomFont() {
        PdfFont customFont;
        try {
            customFont = PdfFontFactory.createFont(FONT_PATH, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            System.err.println("Error al cargar la fuente personalizada: " + e.getMessage() + ". Usando Helvetica como respaldo.");
            try {
                customFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            } catch (IOException ex) {
                System.err.println("ERROR FATAL: No se pudo cargar ninguna fuente. " + ex.getMessage());
                throw new RuntimeException("No se pudo cargar ninguna fuente para el PDF.", ex);
            }
        }
        return customFont;
    }

    public static DeviceRgb getHeaderColor() {
        return HEADER_COLOR;
    }
}