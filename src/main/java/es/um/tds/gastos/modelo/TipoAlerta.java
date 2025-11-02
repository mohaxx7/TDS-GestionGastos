package es.um.tds.gastos.modelo;

/**
 * Tipos de alertas disponibles en el sistema.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public enum TipoAlerta {
    SEMANAL("Semanal"),
    MENSUAL("Mensual");
    
    private final String descripcion;
    
    TipoAlerta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
