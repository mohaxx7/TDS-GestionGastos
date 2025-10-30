package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz para la gestión de gastos.
 * Define las operaciones de negocio relacionadas con gastos.
 * 
 * Patrón: Facade
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IGestorGastos {
    
    /**
     * Registra un nuevo gasto en el sistema
     */
    Gasto registrarGasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria);
    
    /**
     * Edita un gasto existente
     */
    Gasto editarGasto(int id, double cantidad, LocalDate fecha, String descripcion, Categoria categoria);
    
    /**
     * Elimina un gasto del sistema
     */
    boolean eliminarGasto(int id);
    
    /**
     * Obtiene todos los gastos registrados
     */
    List<Gasto> obtenerTodosLosGastos();
    
    /**
     * Busca un gasto por su ID
     */
    Gasto buscarGastoPorId(int id);
    
    /**
     * Obtiene gastos filtrados por rango de fechas
     */
    List<Gasto> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Obtiene gastos filtrados por categoría
     */
    List<Gasto> filtrarPorCategoria(Categoria categoria);
    
    /**
     * Obtiene gastos filtrados por lista de categorías
     */
    List<Gasto> filtrarPorCategorias(List<Categoria> categorias);
}
