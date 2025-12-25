package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia que calcula los gastos del mes actual.
 * Implementa el patron Estrategia para el calculo de gastos mensuales,
 * filtrando los gastos desde el primer dia hasta el ultimo del mes.
 * 
 * @author Grupo TDS
 * @version 1.0
 */
public class EstrategiaMensual implements EstrategiaCalculoPeriodo {

    /**
     * Filtra los gastos que pertenecen al mes de la fecha de referencia.
     * El rango incluye desde el dia 1 hasta el ultimo dia del mes.
     * 
     * @param gastos          lista de todos los gastos a filtrar
     * @param fechaReferencia fecha que determina el mes a considerar
     * @return lista de gastos realizados en el mes
     */
    @Override
    public List<Gasto> filtrarGastosPeriodo(List<Gasto> gastos, LocalDate fechaReferencia) {
        if (gastos == null || fechaReferencia == null) {
            return List.of();
        }

        YearMonth mes = YearMonth.from(fechaReferencia);
        LocalDate inicioMes = mes.atDay(1);
        LocalDate finMes = mes.atEndOfMonth();

        return gastos.stream()
                .filter(g -> !g.getFecha().isBefore(inicioMes)
                        && !g.getFecha().isAfter(finMes))
                .collect(Collectors.toList());
    }

    /**
     * Calcula la suma total de los gastos del mes.
     * 
     * @param gastos          lista de todos los gastos
     * @param fechaReferencia fecha que determina el mes a considerar
     * @return suma de las cantidades de los gastos del mes
     */
    @Override
    public double calcularTotalPeriodo(List<Gasto> gastos, LocalDate fechaReferencia) {
        List<Gasto> gastosMes = filtrarGastosPeriodo(gastos, fechaReferencia);

        return gastosMes.stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }

    /**
     * Devuelve la descripcion del periodo que maneja esta estrategia.
     * 
     * @return la cadena "mensual"
     */
    @Override
    public String getDescripcionPeriodo() {
        return "mensual";
    }
}
