package es.um.tds.gastos.modelo;

import java.util.Arrays;
import java.util.List;

/**
 * Representa una categoría de gasto en el sistema.
 * Las categorías permiten organizar los gastos.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Categoria {
    
    private int id;
    private String nombre;
    
    // Categorías predefinidas del sistema
    public static final String ALIMENTACION = "Alimentación";
    public static final String TRANSPORTE = "Transporte";
    public static final String OCIO = "Ocio";
    public static final String SALUD = "Salud";
    public static final String HOGAR = "Hogar";
    public static final String OTROS = "Otros";
    
    /**
     * Constructor básico de Categoria
     */
    public Categoria() {
        this.id = 0;
        this.nombre = "";
    }
    
    /**
     * Constructor con parámetros
     * @param nombre Nombre de la categoría
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la lista de categorías predefinidas del sistema
     * @return Lista de nombres de categorías predefinidas
     */
    public static List<String> obtenerCategoriasPredefinidas() {
        return Arrays.asList(ALIMENTACION, TRANSPORTE, OCIO, SALUD, HOGAR, OTROS);
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
