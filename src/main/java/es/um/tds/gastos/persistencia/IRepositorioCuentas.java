package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.CuentaCompartida;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de cuentas compartidas.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IRepositorioCuentas {
    
    /**
     * Guarda una nueva cuenta compartida
     */
    CuentaCompartida guardar(CuentaCompartida cuenta);
    
    /**
     * Actualiza una cuenta compartida existente
     */
    CuentaCompartida actualizar(CuentaCompartida cuenta);
    
    /**
     * Elimina una cuenta compartida
     */
    boolean eliminar(int id);
    
    /**
     * Busca una cuenta por su ID
     */
    Optional<CuentaCompartida> buscarPorId(int id);
    
    /**
     * Busca una cuenta por su nombre
     */
    Optional<CuentaCompartida> buscarPorNombre(String nombre);
    
    /**
     * Obtiene todas las cuentas compartidas
     */
    List<CuentaCompartida> obtenerTodas();
}
