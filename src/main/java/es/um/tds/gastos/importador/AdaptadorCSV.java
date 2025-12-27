package es.um.tds.gastos.importador;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para importar gastos desde archivos CSV.
 * Convierte el formato CSV externo al formato interno de la aplicacion.
 * 
 * Patron aplicado: Adapter (Concrete Adapter)
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class AdaptadorCSV implements AdaptadorImportacion {

    private static final String EXTENSION_CSV = ".csv";
    private static final String SEPARADOR = ",";

    @Override
    public List<Gasto> importarGastos(String rutaArchivo) throws Exception {
        List<Gasto> gastos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            // Saltar cabecera
            String linea = reader.readLine();

            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Gasto gasto = parsearLinea(linea);
                    if (gasto != null) {
                        gastos.add(gasto);
                    }
                }
            }
        }

        return gastos;
    }

    @Override
    public boolean soportaFormato(String nombreArchivo) {
        return nombreArchivo != null &&
                nombreArchivo.toLowerCase().endsWith(EXTENSION_CSV);
    }

    /**
     * Parsea una linea CSV y crea un objeto Gasto.
     * Formato esperado:
     * Date,Account,Category,Subcategory,Note,Payer,Amount,Currency
     */
    private Gasto parsearLinea(String linea) {
        try {
            String[] campos = linea.split(SEPARADOR);

            // Campos del CSV
            String fechaStr = campos[0]; // Date
            String subcategoria = campos[3]; // Subcategory (usamos como categoria)
            String descripcion = campos[4]; // Note
            double cantidad = Double.parseDouble(campos[6]); // Amount

            // Parsear fecha (formato: M/d/yyyy HH:mm)
            LocalDate fecha = parsearFecha(fechaStr);

            // Crear categoria
            Categoria categoria = new Categoria(subcategoria);

            return new Gasto(cantidad, fecha, descripcion, categoria);

        } catch (Exception e) {
            System.err.println("Error parseando linea: " + linea);
            return null;
        }
    }

    /**
     * Parsea la fecha del formato CSV al formato interno.
     */
    private LocalDate parsearFecha(String fechaStr) {
        // Formato: M/d/yyyy HH:mm -> extraer solo la fecha
        String soloFecha = fechaStr.split(" ")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return LocalDate.parse(soloFecha, formatter);
    }
}
