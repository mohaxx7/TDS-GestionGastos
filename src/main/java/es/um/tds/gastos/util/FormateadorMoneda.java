package es.um.tds.gastos.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilidad para formatear cantidades monetarias.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class FormateadorMoneda {
    
    private static final Locale LOCALE_ESPANA = new Locale("es", "ES");
    private static final NumberFormat formatoEuros = NumberFormat.getCurrencyInstance(LOCALE_ESPANA);
    
    /**
     * Formatea una cantidad a formato de euros español (ej: 123,45 €)
     */
    public static String formatearEuros(double cantidad) {
        return formatoEuros.format(cantidad);
    }
    
    /**
     * Formatea una cantidad sin símbolo de moneda (ej: 123,45)
     */
    public static String formatearSinSimbolo(double cantidad) {
        return String.format(LOCALE_ESPANA, "%.2f", cantidad);
    }
    
    /**
     * Parsea una cadena a double
     */
    public static double parsear(String cantidad) {
        if (cantidad == null || cantidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La cantidad no puede estar vacía");
        }
        
        // Eliminar símbolos de moneda y espacios
        String limpio = cantidad.replace("€", "").replace(" ", "").replace(",", ".");
        
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de cantidad inválido: " + cantidad);
        }
    }
}
