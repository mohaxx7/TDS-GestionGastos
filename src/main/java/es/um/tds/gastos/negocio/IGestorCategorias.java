package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import java.util.List;

/**
 * Interfaz para la gestión de categorías.
 * Define las operaciones de negocio relacionadas con categorías.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IGestorCategorias {
    
    /**
     * Crea una nueva categoría personalizada
     */
    Categoria crearCategoria(String nombre);
    
    /**
     * Obtiene todas las categorías disponibles
     */
    List<Categoria> obtenerTodasLasCategorias();
    
    /**
     * Busca una categoría por su nombre
     */
    Categoria buscarCategoriaPorNombre(String nombre);
    
    /**
     * Busca una categoría por su ID
     */
    Categoria buscarCategoriaPorId(int id);
}
