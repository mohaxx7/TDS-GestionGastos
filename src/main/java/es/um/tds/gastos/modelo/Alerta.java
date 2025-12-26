package es.um.tds.gastos.modelo;

import es.um.tds.gastos.negocio.EstrategiaCalculoPeriodo;
import es.um.tds.gastos.negocio.EstrategiaSemanal;
import es.um.tds.gastos.negocio.EstrategiaMensual;

/**
 * Representa una alerta de gasto configurable por el usuario.
 * Utiliza el patron Estrategia para calcular los gastos del periodo
 * correspondiente segun el tipo de alerta (semanal o mensual).
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
    private transient EstrategiaCalculoPeriodo estrategia;

    /**
     * Constructor por defecto requerido para la deserializacion JSON.
     * No debe usarse directamente, utilizar el constructor con parametros.
     */
    public Alerta() {
    }

    /**
     * Crea una alerta general sin categoria asociada.
     * 
     * @param tipo        tipo de alerta (semanal o mensual)
     * @param limiteGasto limite de gasto en euros
     */
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

    /**
     * Obtiene la estrategia de calculo de periodo correspondiente al tipo de
     * alerta.
     * Implementa el patron Estrategia devolviendo la estrategia adecuada segun
     * si la alerta es semanal o mensual.
     * 
     * @return la estrategia de calculo (EstrategiaSemanal o EstrategiaMensual)
     */
    public EstrategiaCalculoPeriodo getEstrategia() {
        if (estrategia == null) {
            if (tipo == TipoAlerta.SEMANAL) {
                estrategia = new EstrategiaSemanal();
            } else {
                estrategia = new EstrategiaMensual();
            }
        }
        return estrategia;
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
