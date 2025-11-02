package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.CuentaCompartida;
import es.um.tds.gastos.modelo.Gasto;
import es.um.tds.gastos.modelo.PersonaCuenta;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interfaz para la gestión de cuentas compartidas.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IGestorCuentas {
    
    /**
     * Crea una nueva cuenta compartida
     */
    CuentaCompartida crearCuenta(String nombreCuenta, List<PersonaCuenta> personas);
    
    /**
     * Registra un gasto en una cuenta compartida
     */
    Gasto registrarGastoEnCuenta(int idCuenta, double cantidad, LocalDate fecha, 
                                  String descripcion, Categoria categoria, PersonaCuenta pagador);
    
    /**
     * Configura porcentajes personalizados para una cuenta
     */
    void configurarPorcentajes(int idCuenta, Map<PersonaCuenta, Double> porcentajes);
    
    /**
     * Obtiene todas las cuentas compartidas
     */
    List<CuentaCompartida> obtenerTodasLasCuentas();
    
    /**
     * Busca una cuenta por su ID
     */
    CuentaCompartida buscarCuentaPorId(int id);
    
    /**
     * Obtiene los saldos de todas las personas de una cuenta
     */
    Map<PersonaCuenta, Double> obtenerSaldos(int idCuenta);
    
    /**
     * Obtiene el total gastado en una cuenta
     */
    double obtenerTotalGastado(int idCuenta);
}
