package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilidad para filtrar gastos según diferentes criterios.
 * Implementa el patrón Strategy para filtrado.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class FiltroGastos {

    /**
     * Filtra gastos por un rango de fechas
     */
    public static List<Gasto> filtrarPorRangoFechas(List<Gasto> gastos, LocalDate inicio, LocalDate fin) {
        if (gastos == null)
            return List.of();
        if (inicio == null || fin == null)
            return gastos;

        return gastos.stream()
                .filter(g -> !g.getFecha().isBefore(inicio) && !g.getFecha().isAfter(fin))
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos por un mes específico
     */
    public static List<Gasto> filtrarPorMes(List<Gasto> gastos, YearMonth mes) {
        if (gastos == null || mes == null)
            return gastos;

        LocalDate inicio = mes.atDay(1);
        LocalDate fin = mes.atEndOfMonth();

        return filtrarPorRangoFechas(gastos, inicio, fin);
    }

    /**
     * Filtra gastos por una lista de meses
     */
    public static List<Gasto> filtrarPorMeses(List<Gasto> gastos, List<YearMonth> meses) {
        if (gastos == null || meses == null || meses.isEmpty())
            return gastos;

        return gastos.stream()
                .filter(g -> {
                    YearMonth mesGasto = YearMonth.from(g.getFecha());
                    return meses.contains(mesGasto);
                })
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos por categoría
     */
    public static List<Gasto> filtrarPorCategoria(List<Gasto> gastos, Categoria categoria) {
        if (gastos == null || categoria == null)
            return gastos;

        return gastos.stream()
                .filter(g -> g.getCategoria().getId() == categoria.getId())
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos por lista de categorías
     */
    public static List<Gasto> filtrarPorCategorias(List<Gasto> gastos, List<Categoria> categorias) {
        if (gastos == null || categorias == null || categorias.isEmpty())
            return gastos;

        List<Integer> idsCategorias = categorias.stream()
                .map(Categoria::getId)
                .collect(Collectors.toList());

        return gastos.stream()
                .filter(g -> idsCategorias.contains(g.getCategoria().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos por cantidad mínima
     */
    public static List<Gasto> filtrarPorCantidadMinima(List<Gasto> gastos, double cantidadMinima) {
        if (gastos == null)
            return List.of();

        return gastos.stream()
                .filter(g -> g.getCantidad() >= cantidadMinima)
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos por cantidad máxima
     */
    public static List<Gasto> filtrarPorCantidadMaxima(List<Gasto> gastos, double cantidadMaxima) {
        if (gastos == null)
            return List.of();

        return gastos.stream()
                .filter(g -> g.getCantidad() <= cantidadMaxima)
                .collect(Collectors.toList());
    }

    /**
     * Filtra gastos que contengan un texto en la descripción
     */
    public static List<Gasto> filtrarPorDescripcion(List<Gasto> gastos, String textoBusqueda) {
        if (gastos == null || textoBusqueda == null || textoBusqueda.trim().isEmpty())
            return gastos;

        String busqueda = textoBusqueda.toLowerCase();
        return gastos.stream()
                .filter(g -> g.getDescripcion().toLowerCase().contains(busqueda))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el total de una lista de gastos
     */
    public static double calcularTotal(List<Gasto> gastos) {
        if (gastos == null || gastos.isEmpty())
            return 0.0;

        return gastos.stream()
                .mapToDouble(Gasto::getCantidad)
                .sum();
    }

    /**
     * Calcula el promedio de una lista de gastos
     */
    public static double calcularPromedio(List<Gasto> gastos) {
        if (gastos == null || gastos.isEmpty())
            return 0.0;

        return gastos.stream()
                .mapToDouble(Gasto::getCantidad)
                .average()
                .orElse(0.0);
    }
}
