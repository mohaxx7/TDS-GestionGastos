package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia que calcula los gastos de la ultima semana (7 dias).
 * Implementa el patron Estrategia para el calculo de gastos semanales,
 * permitiendo que las alertas semanales utilicen este algoritmo.
 * 
 * @author Grupo TDS
 * @version 1.0
 */
public class EstrategiaSemanal implements EstrategiaCalculoPeriodo {

    private static final int DIAS_SEMANA = 7;

    /**
     * Filtra los gastos que se han realizado en los ultimos 7 dias.
     * El rango incluye desde hace 7 dias hasta la fecha de referencia.
     * 
     * @param gastos          lista de todos los gastos a filtrar
     * @param fechaReferencia fecha desde la que contar hacia atras
     * @return lista de gastos realizados en la ultima semana
     */
    @Override
    public List<Gasto> filtrarGastosPeriodo(List<Gasto> gastos, LocalDate fechaReferencia) {
        if (gastos == null || fechaReferencia == null) {
            return List.of();
        }

        LocalDate inicioSemana = fechaReferencia.minusDays(DIAS_SEMANA);

        return gastos.stream()
                .filter(g -> !g.getFecha().isBefore(inicioSemana)
                        && !g.getFecha().isAfter(fechaReferencia))
                .collect(Collectors.toList());
    }

    /**
     * Calcula la suma total de los gastos de la ultima semana.
     * 
     * @param gastos          lista de todos los gastos
     * @param fechaReferencia fecha desde la que contar hacia atras
     * @return suma de las cantidades de los gastos de la semana
     */
    @Override
    public double calcularTotalPeriodo(List<Gasto> gastos, LocalDate fechaReferencia) {
        List<Gasto> gastosSemana = filtrarGastosPeriodo(gastos, fechaReferencia);

        return gastosSemana.stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }

    /**
     * Devuelve la descripcion del periodo que maneja esta estrategia.
     * 
     * @return la cadena "semanal"
     */
    @Override
    public String getDescripcionPeriodo() {
        return "semanal";
    }
}
