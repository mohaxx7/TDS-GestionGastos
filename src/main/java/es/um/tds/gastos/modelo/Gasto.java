package es.um.tds.gastos.modelo;

import java.time.LocalDate;

/**
 * Representa un gasto registrado en el sistema.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Gasto {
    
    private int id;
    private double cantidad;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    
    /**
     * Constructor completo de Gasto
     * @throws IllegalArgumentException si los parámetros no son válidos
     */
    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public double getCantidad() {
        return cantidad;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setCantidad(double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.cantidad = cantidad;
    }
    
    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        this.fecha = fecha;
    }
    
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        this.descripcion = descripcion;
    }
    
    public void setCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return String.format("Gasto[id=%d, cantidad=%.2f€, fecha=%s, categoría=%s]", 
                             id, cantidad, fecha, categoria.getNombre());
    }
}
