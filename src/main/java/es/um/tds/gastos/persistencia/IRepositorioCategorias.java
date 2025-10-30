package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Categoria;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de categorías.
 * Define las operaciones CRUD básicas.
 * 
 * Patrón: Repository Pattern
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IRepositorioCategorias {
    
    /**
     * Guarda una nueva categoría en el repositorio
     * @param categoria La categoría a guardar
     * @return La categoría guardada con su ID asignado
     */
    Categoria guardar(Categoria categoria);
    
    /**
     * Actualiza una categoría existente
     * @param categoria La categoría a actualizar
     * @return La categoría actualizada
     */
    Categoria actualizar(Categoria categoria);
    
    /**
     * Elimina una categoría del repositorio
     * @param id El ID de la categoría a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);
    
    /**
     * Busca una categoría por su ID
     * @param id El ID de la categoría
     * @return Optional con la categoría si existe, Optional.empty() en caso contrario
     */
    Optional<Categoria> buscarPorId(int id);
    
    /**
     * Busca una categoría por su nombre
     * @param nombre El nombre de la categoría
     * @return Optional con la categoría si existe, Optional.empty() en caso contrario
     */
    Optional<Categoria> buscarPorNombre(String nombre);
    
    /**
     * Obtiene todas las categorías del repositorio
     * @return Lista de todas las categorías
     */
    List<Categoria> obtenerTodas();
    
    /**
     * Inicializa las categorías predefinidas del sistema
     */
    void inicializarCategoriasPredefinidas();
}
