package es.um.tds.gastos.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilidad para validar y formatear fechas.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class ValidadorFechas {
    
    private static final DateTimeFormatter FORMATO_ESTANDAR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ISO = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Valida que una fecha sea válida
     */
    public static boolean esFechaValida(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now());
    }
    
    /**
     * Valida que un rango de fechas sea válido
     */
    public static boolean esRangoValido(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) return false;
        return !inicio.isAfter(fin);
    }
    
    /**
     * Formatea una fecha al formato español (dd/MM/yyyy)
     */
    public static String formatear(LocalDate fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_ESTANDAR);
    }
    
    /**
     * Parsea una cadena a LocalDate usando formato español
     */
    public static LocalDate parsear(String fecha) throws DateTimeParseException {
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        
        try {
            return LocalDate.parse(fecha, FORMATO_ESTANDAR);
        } catch (DateTimeParseException e) {
            // Intentar con formato ISO
            return LocalDate.parse(fecha, FORMATO_ISO);
        }
    }
    
    /**
     * Parsea una cadena de forma segura, devolviendo null si hay error
     */
    public static LocalDate parsearSeguro(String fecha) {
        try {
            return parsear(fecha);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Obtiene la fecha de inicio del mes actual
     */
    public static LocalDate inicioMesActual() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    /**
     * Obtiene la fecha de fin del mes actual
     */
    public static LocalDate finMesActual() {
        LocalDate hoy = LocalDate.now();
        return hoy.withDayOfMonth(hoy.lengthOfMonth());
    }
}
