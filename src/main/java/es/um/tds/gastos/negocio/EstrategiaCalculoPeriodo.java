package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define la estrategia para calcular los gastos de un periodo.
 * Implementa el patron Estrategia permitiendo intercambiar el algoritmo de
 * calculo del periodo (semanal, mensual) sin modificar el codigo cliente.
 * 
 * @author Grupo TDS
 * @version 1.0
 */
public interface EstrategiaCalculoPeriodo {

    /**
     * Filtra los gastos que corresponden al periodo definido por la estrategia.
     * Cada implementacion define como calcular el rango de fechas del periodo.
     * 
     * @param gastos          lista de gastos a filtrar
     * @param fechaReferencia fecha a partir de la cual calcular el periodo
     * @return lista de gastos que pertenecen al periodo
     */
    List<Gasto> filtrarGastosPeriodo(List<Gasto> gastos, LocalDate fechaReferencia);

    /**
     * Calcula el total de gastos del periodo.
     * 
     * @param gastos          lista de gastos a procesar
     * @param fechaReferencia fecha a partir de la cual calcular el periodo
     * @return suma total de los gastos del periodo
     */
    double calcularTotalPeriodo(List<Gasto> gastos, LocalDate fechaReferencia);

    /**
     * Obtiene la descripcion del tipo de periodo que maneja esta estrategia.
     * 
     * @return descripcion del periodo (ej: "semanal", "mensual")
     */
    String getDescripcionPeriodo();
}
