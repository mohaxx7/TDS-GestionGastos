package es.um.tds.gastos.servicios;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import es.um.tds.gastos.modelo.Gasto;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para generar archivos PDF con la lista de gastos.
 * Implementa el patrón Servicio para encapsular el acceso a la librería iText.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GeneradorPDF {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Genera un archivo PDF con la lista de gastos.
     * 
     * @param gastos      Lista de gastos a incluir en el PDF
     * @param rutaArchivo Ruta donde guardar el PDF
     * @throws FileNotFoundException Si no se puede crear el archivo
     */
    public void generarPDF(List<Gasto> gastos, String rutaArchivo) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título
        Paragraph titulo = new Paragraph("Informe de Gastos")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titulo);

        // Fecha de generación
        Paragraph fecha = new Paragraph("Generado el: " + java.time.LocalDate.now().format(DATE_FORMAT))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(fecha);

        document.add(new Paragraph("\n"));

        // Tabla de gastos
        Table tabla = new Table(UnitValue.createPercentArray(new float[] { 15, 20, 35, 30 }))
                .setWidth(UnitValue.createPercentValue(100));

        // Cabeceras
        tabla.addHeaderCell(new Cell().add(new Paragraph("Cantidad").setBold()));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Fecha").setBold()));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Descripción").setBold()));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Categoría").setBold()));

        // Datos
        double total = 0;
        for (Gasto gasto : gastos) {
            tabla.addCell(new Cell().add(new Paragraph(String.format("%.2f €", gasto.getCantidad()))));
            tabla.addCell(new Cell().add(new Paragraph(gasto.getFecha().format(DATE_FORMAT))));
            tabla.addCell(new Cell().add(new Paragraph(gasto.getDescripcion())));
            tabla.addCell(new Cell().add(new Paragraph(gasto.getCategoria().getNombre())));
            total += gasto.getCantidad();
        }

        document.add(tabla);

        // Total
        document.add(new Paragraph("\n"));
        Paragraph totalParrafo = new Paragraph(String.format("TOTAL: %.2f €", total))
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(totalParrafo);

        // Estadísticas
        if (!gastos.isEmpty()) {
            document.add(new Paragraph("\n--- Estadísticas ---").setBold());
            document.add(new Paragraph(String.format("Número de gastos: %d", gastos.size())));
            document.add(new Paragraph(String.format("Media por gasto: %.2f €", total / gastos.size())));
        }

        document.close();
    }
}
