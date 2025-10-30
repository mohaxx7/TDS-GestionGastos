package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Gasto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de gastos.
 * Define las operaciones CRUD básicas.
 * 
 * Patrón: Repository Pattern
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IRepositorioGastos {
    
    /**
     * Guarda un nuevo gasto en el repositorio
     * @param gasto El gasto a guardar
     * @return El gasto guardado con su ID asignado
     */
    Gasto guardar(Gasto gasto);
    
    /**
     * Actualiza un gasto existente
     * @param gasto El gasto a actualizar
     * @return El gasto actualizado
     */
    Gasto actualizar(Gasto gasto);
    
    /**
     * Elimina un gasto del repositorio
     * @param id El ID del gasto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);
    
    /**
     * Busca un gasto por su ID
     * @param id El ID del gasto
     * @return Optional con el gasto si existe, Optional.empty() en caso contrario
     */
    Optional<Gasto> buscarPorId(int id);
    
    /**
     * Obtiene todos los gastos del repositorio
     * @return Lista de todos los gastos
     */
    List<Gasto> obtenerTodos();
    
    /**
     * Elimina todos los gastos del repositorio
     */
    void eliminarTodos();
}
