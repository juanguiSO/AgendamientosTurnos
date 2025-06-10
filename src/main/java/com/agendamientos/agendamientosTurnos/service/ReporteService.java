package com.agendamientos.agendamientosTurnos.service;

import com.agendamientos.agendamientosTurnos.entity.*;
import com.agendamientos.agendamientosTurnos.repository.EstadoViajeRepository;
import com.agendamientos.agendamientosTurnos.repository.FuncionarioRepository;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReporteService {


    private MisionRepository misionRepository;
    private final FuncionarioRepository funcionarioRepository;
    private ViajeRepository viajeRepository;
    private EstadoViajeRepository estadoViajeRepository;

    @PersistenceContext // Inyecta el EntityManager
    private EntityManager entityManager;

    @Autowired
    public ReporteService(MisionRepository misionRepository,FuncionarioRepository funcionarioRepository, ViajeRepository viajeRepository, EstadoViajeRepository estadoViajeRepository) {
        this.misionRepository= misionRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.viajeRepository = viajeRepository;
        this.estadoViajeRepository = estadoViajeRepository;
    }

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
    /**
     * Obtiene misiones finalizadas para un funcionario en un rango de fechas.
     * Utiliza una consulta SQL nativa para mayor control sobre el filtro de estado y fechas.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicio El inicio del rango de fechas (opcional).
     * @param fechaFin El fin del rango de fechas (opcional).
     * @return Una lista de Object[] donde cada array representa una fila de la consulta SQL.
     */
    @SuppressWarnings("unchecked") // Suprimir advertencia de tipo sin verificar
    public List<Object[]> obtenerMisionesFinalizadasPorFuncionarioYRango(
            String funcionarioCedula, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    f.nombre AS nombre_funcionario, ");
        sql.append("    f.apellido AS apellido_funcionario, ");
        sql.append("    f.cedula AS cedula_funcionario, ");
        sql.append("    m.numero_mision, ");
        //sql.append("    v.id_viaje, ");
        sql.append("    v.tiempo_inicio, ");
        sql.append("    v.tiempo_fin, ");
        sql.append("    ev.estado_viaje AS estado_actual_viaje ");
        sql.append("FROM ");
        sql.append("    funcionario AS f ");
        sql.append("JOIN ");
        sql.append("    mision AS m ON f.id_funcionario = m.id_funcionario ");
        sql.append("JOIN ");
        sql.append("    mision_x_viaje AS mxv ON m.numero_mision = mxv.numero_mision ");
        sql.append("JOIN ");
        sql.append("    viaje AS v ON mxv.id_viaje = v.id_viaje ");
        sql.append("JOIN ");
        sql.append("    estado_viaje AS ev ON v.id_estado_viaje = ev.id_estado_viaje ");
        sql.append("WHERE ");
        sql.append("    f.cedula = :cedulaFuncionario ");
        sql.append("    AND ev.estado_viaje = 'Finalizado'"); // 'Finalizado' o 'finalizado' según tu BD

        if (fechaInicio != null) {
            sql.append("    AND v.tiempo_fin >= :fechaInicio");
        }
        if (fechaFin != null) {
            sql.append("    AND v.tiempo_fin <= :fechaFin");
        }
        // También podrías considerar filtrar por tiempo_inicio si te interesa el rango de "inicio" de la misión/viaje
        // sql.append("    AND v.tiempo_inicio BETWEEN :fechaInicio AND :fechaFin");


        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("cedulaFuncionario", funcionarioCedula);

        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        return query.getResultList();
    }


    /**
     * Obtiene viajes reprogramados o cancelados para un funcionario en un rango de fechas.
     * Incluye el campo de observación del viaje.
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicio El inicio del rango de fechas (opcional).
     * @param fechaFin El fin del rango de fechas (opcional).
     * @return Una lista de Object[] donde cada array representa una fila de la consulta SQL.
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> obtenerViajesReprogramadosOCanceladosPorFuncionarioYRango(
            String funcionarioCedula, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    f.nombre AS nombre_funcionario, ");
        sql.append("    f.apellido AS apellido_funcionario, ");
        sql.append("    f.cedula AS cedula_funcionario, ");
        sql.append("    m.numero_mision, ");
        //sql.append("    v.id_viaje, ");
        sql.append("    v.tiempo_inicio, ");
        sql.append("    v.tiempo_fin, ");
        sql.append("    ev.estado_viaje AS estado_actual_viaje, ");
        sql.append("    v.observacion "); // ¡Campo nuevo añadido!
        sql.append("FROM ");
        sql.append("    funcionario AS f ");
        sql.append("JOIN ");
        sql.append("    mision AS m ON f.id_funcionario = m.id_funcionario ");
        sql.append("JOIN ");
        sql.append("    mision_x_viaje AS mxv ON m.numero_mision = mxv.numero_mision ");
        sql.append("JOIN ");
        sql.append("    viaje AS v ON mxv.id_viaje = v.id_viaje ");
        sql.append("JOIN ");
        sql.append("    estado_viaje AS ev ON v.id_estado_viaje = ev.id_estado_viaje ");
        sql.append("WHERE ");
        sql.append("    f.cedula = :cedulaFuncionario ");
        sql.append("    AND ev.estado_viaje IN ('Reprogramado', 'Cancelado')"); // Asegúrate de que los estados sean correctos

        if (fechaInicio != null) {
            sql.append("    AND v.tiempo_inicio >= :fechaInicio");
        }
        if (fechaFin != null) {
            sql.append("    AND v.tiempo_inicio <= :fechaFin");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("cedulaFuncionario", funcionarioCedula);

        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        return query.getResultList();
    }

    /**
     * Obtiene viajes realizados por un funcionario en un rango de fechas,
     * y los clasifica en viajes con viáticos y sin viáticos.
     * Asume que la tabla 'viaje' tiene una columna 'viatico' (boolean).
     * @param funcionarioCedula La cédula del funcionario.
     * @param fechaInicio El inicio del rango de fechas (opcional).
     * @param fechaFin El fin del rango de fechas (opcional).
     * @return Un Map que contiene dos listas: "conViaticos" y "sinViaticos".
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<Object[]>> obtenerViajesConSinViaticosPorFuncionarioYRango(
            String funcionarioCedula, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    f.nombre AS nombre_funcionario, ");
        sql.append("    f.apellido AS apellido_funcionario, ");
        sql.append("    f.cedula AS cedula_funcionario, ");
        sql.append("    m.numero_mision, ");
        sql.append("    v.tiempo_inicio, ");
        sql.append("    v.tiempo_fin, ");
        sql.append("    ev.estado_viaje AS estado_actual_viaje, ");
        sql.append("    v.viatico "); // ¡CAMBIO AQUÍ: v.viatico en lugar de v.tiene_viaticos!
        sql.append("FROM ");
        sql.append("    funcionario AS f ");
        sql.append("JOIN ");
        sql.append("    mision AS m ON f.id_funcionario = m.id_funcionario ");
        sql.append("JOIN ");
        sql.append("    mision_x_viaje AS mxv ON m.numero_mision = mxv.numero_mision ");
        sql.append("JOIN ");
        sql.append("    viaje AS v ON mxv.id_viaje = v.id_viaje ");
        sql.append("JOIN ");
        sql.append("    estado_viaje AS ev ON v.id_estado_viaje = ev.id_estado_viaje ");
        sql.append("WHERE ");
        sql.append("    f.cedula = :cedulaFuncionario ");
        sql.append("    AND ev.estado_viaje = 'Finalizado'");

        if (fechaInicio != null) {
            sql.append("    AND v.tiempo_inicio >= :fechaInicio");
        }
        if (fechaFin != null) {
            sql.append("    AND v.tiempo_fin <= :fechaFin");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("cedulaFuncionario", funcionarioCedula);

        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        List<Object[]> resultados = query.getResultList();

        List<Object[]> viajesConViaticos = new ArrayList<>();
        List<Object[]> viajesSinViaticos = new ArrayList<>();

        // Procesar los resultados y separar por viáticos
        for (Object[] row : resultados) {
            // El campo viatico será el último elemento en el array
            // El índice sigue siendo 7 (si contamos desde 0, porque tenemos 8 columnas ahora)
            // f.nombre (0), f.apellido (1), f.cedula (2), m.numero_mision (3),
            // v.tiempo_inicio (4), v.tiempo_fin (5), ev.estado_viaje (6), v.viatico (7)
            Boolean viatico = (Boolean) row[7]; // Se asume que tu DB mapea el tipo booleano directamente a Boolean en Java.

            // Si por alguna razón tu DB devuelve un Integer (0 o 1) para un booleano (como con TINYINT(1) en MySQL),
            // usarías la siguiente línea en su lugar:
            // Boolean viatico = (row[7] != null && ((Number) row[7]).intValue() == 1);

            if (viatico != null && viatico) { // Comprueba que no sea nulo y que sea true
                viajesConViaticos.add(row);
            } else {
                viajesSinViaticos.add(row);
            }
        }

        Map<String, List<Object[]>> viajesPorCategoria = new LinkedHashMap<>();
        viajesPorCategoria.put("conViaticos", viajesConViaticos);
        viajesPorCategoria.put("sinViaticos", viajesSinViaticos);

        return viajesPorCategoria;
    }

    /**
     * Obtiene todas las misiones que tienen al menos un viaje que no está en estado 'Finalizado' o 'Cancelado'.
     * @return Una lista de Object[] donde cada array representa una fila de la consulta SQL.
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> obtenerMisionesActivas() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT "); // Ya no es necesario DISTINCT si filtramos directamente por m.activo
        sql.append("    m.numero_mision, ");
        sql.append("    f.nombre AS nombre_funcionario, ");
        sql.append("    f.apellido AS apellido_funcionario, ");
        sql.append("    f.cedula AS cedula_funcionario ");

        sql.append("FROM ");
        sql.append("    mision AS m ");
        sql.append("JOIN ");
        sql.append("    funcionario AS f ON m.id_funcionario = f.id_funcionario ");

        sql.append("WHERE ");
        sql.append("    m.activo = TRUE "); // CAMBIO CLAVE: Usamos el campo 'activo'
        sql.append("ORDER BY ");
        sql.append("    m.numero_mision");

        Query query = entityManager.createNativeQuery(sql.toString());

        return query.getResultList();
    }

    /**
     * Obtiene los viajes finalizados por un vehículo específico en un rango de fechas.
     * @param idVehiculo El ID del vehículo.
     * @param fechaInicio El inicio del rango de fechas (opcional).
     * @param fechaFin El fin del rango de fechas (opcional).
     * @return Una lista de Object[] donde cada array representa una fila de la consulta SQL.
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> obtenerViajesFinalizadosPorVehiculoYRango(
            Integer idVehiculo, LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    v.id_viaje, ");
        sql.append("    veh.marca, ");
        sql.append("    veh.modelo, ");
        sql.append("    veh.placa, ");
        sql.append("    v.tiempo_inicio, ");
        sql.append("    v.tiempo_fin, ");
        sql.append("    ev.estado_viaje AS estado_actual_viaje, ");
        sql.append("    v.distancia_recorrida, ");
        sql.append("    v.observacion, ");
        sql.append("    v.viatico, "); // Incluimos si tuvo viático
        sql.append("    f.nombre AS nombre_funcionario, "); // Incluimos el funcionario asociado si lo hay
        sql.append("    f.apellido AS apellido_funcionario ");
        sql.append("FROM ");
        sql.append("    viaje AS v ");
        sql.append("JOIN ");
        sql.append("    vehiculo AS veh ON v.id_vehiculo = veh.id_vehiculo ");
        sql.append("JOIN ");
        sql.append("    estado_viaje AS ev ON v.id_estado_viaje = ev.id_estado_viaje ");
        sql.append("LEFT JOIN "); // LEFT JOIN porque un viaje podría no estar asociado directamente a una misión_x_viaje
        sql.append("    mision_x_viaje AS mxv ON v.id_viaje = mxv.id_viaje ");
        sql.append("LEFT JOIN "); // LEFT JOIN por si la misión no tiene funcionario o no se quiere mostrar
        sql.append("    mision AS m ON mxv.numero_mision = m.numero_mision ");
        sql.append("LEFT JOIN ");
        sql.append("    funcionario AS f ON m.id_funcionario = f.id_funcionario ");
        sql.append("WHERE ");
        sql.append("    v.id_vehiculo = :idVehiculo ");
        sql.append("    AND ev.estado_viaje = 'Finalizado'");

        if (fechaInicio != null) {
            sql.append("    AND v.tiempo_inicio >= :fechaInicio");
        }
        if (fechaFin != null) {
            sql.append("    AND v.tiempo_fin <= :fechaFin");
        }
        sql.append(" ORDER BY v.tiempo_fin DESC"); // Ordenar por fecha de finalización

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idVehiculo", idVehiculo);

        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        return query.getResultList();
    }
    /**
     * Obtiene una lista de todos los vehículos junto con su estado actual (texto).
     * @return Una lista de Object[] donde cada array representa una fila de la consulta SQL.
     * Las columnas esperadas son: veh.marca, veh.modelo, veh.placa, ev.estado_vehiculo.
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> obtenerVehiculosConEstado() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("    veh.marca, ");
        sql.append("    veh.modelo, ");
        sql.append("    veh.placa, ");
        sql.append("    esv.estado_vehiculo "); // Alias para evitar conflicto si hay otro 'estado_vehiculo'
        sql.append("FROM ");
        sql.append("    vehiculo AS veh ");
        sql.append("JOIN ");
        sql.append("    estado_vehiculo AS esv ON veh.id_estado_vehiculo = esv.id_estado_vehiculo ");
        sql.append("ORDER BY ");
        sql.append("    veh.marca, veh.modelo, veh.placa");

        Query query = entityManager.createNativeQuery(sql.toString());

        return query.getResultList();
    }
}