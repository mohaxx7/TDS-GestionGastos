package es.um.tds.gastos.modelo;

/**
 * Representa una alerta de gasto configurable por el usuario.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Alerta {
    
    private int id;
    private TipoAlerta tipo;
    private double limiteGasto;
    private Categoria categoria;
    private boolean activa;
    
    public Alerta(TipoAlerta tipo, double limiteGasto) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de alerta no puede ser nulo");
        }
        if (limiteGasto <= 0) {
            throw new IllegalArgumentException("El límite de gasto debe ser mayor que cero");
        }
        
        this.tipo = tipo;
        this.limiteGasto = limiteGasto;
        this.categoria = null;
        this.activa = true;
    }
    
    public Alerta(TipoAlerta tipo, double limiteGasto, Categoria categoria) {
        this(tipo, limiteGasto);
        this.categoria = categoria;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public TipoAlerta getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoAlerta tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser nulo");
        }
        this.tipo = tipo;
    }
    
    public double getLimiteGasto() {
        return limiteGasto;
    }
    
    public void setLimiteGasto(double limiteGasto) {
        if (limiteGasto <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor que cero");
        }
        this.limiteGasto = limiteGasto;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public boolean isActiva() {
        return activa;
    }
    
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    public boolean esGeneral() {
        return categoria == null;
    }
    
    public boolean superaLimite(double gastoActual) {
        return gastoActual > limiteGasto;
    }
    
    @Override
    public String toString() {
        String info = String.format("Alerta[tipo=%s, límite=%.2f€", tipo, limiteGasto);
        if (categoria != null) {
            info += ", categoría=" + categoria.getNombre();
        }
        info += ", activa=" + activa + "]";
        return info;
    }
}
