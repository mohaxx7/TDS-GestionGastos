package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Notificacion;
import es.um.tds.gastos.modelo.TipoAlerta;
import java.util.List;

/**
 * Interfaz para el gestor de alertas y notificaciones.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IGestorAlertas {
    
    // Gestión de alertas
    Alerta crearAlerta(TipoAlerta tipo, double limiteGasto);
    Alerta crearAlerta(TipoAlerta tipo, double limiteGasto, Categoria categoria);
    void modificarAlerta(int idAlerta, double nuevoLimite);
    void activarAlerta(int idAlerta);
    void desactivarAlerta(int idAlerta);
    void eliminarAlerta(int idAlerta);
    List<Alerta> obtenerTodasLasAlertas();
    List<Alerta> obtenerAlertasActivas();
    
    // Gestión de notificaciones
    List<Notificacion> obtenerNotificacionesNoLeidas();
    void marcarNotificacionComoLeida(int idNotificacion);
    void marcarTodasLasNotificacionesComoLeidas();
    
    // Verificación de alertas
    void verificarAlertas(List<es.um.tds.gastos.modelo.Gasto> gastos);
}
