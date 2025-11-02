package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Notificacion;
import java.util.List;

/**
 * Interfaz para la persistencia de alertas y notificaciones.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public interface IRepositorioAlertas {
    
    // Operaciones CRUD para Alertas
    Alerta guardarAlerta(Alerta alerta);
    void actualizarAlerta(Alerta alerta);
    void eliminarAlerta(int id);
    Alerta buscarAlertaPorId(int id);
    List<Alerta> obtenerTodasLasAlertas();
    List<Alerta> obtenerAlertasActivas();
    
    // Operaciones CRUD para Notificaciones
    Notificacion guardarNotificacion(Notificacion notificacion);
    void actualizarNotificacion(Notificacion notificacion);
    void eliminarNotificacion(int id);
    Notificacion buscarNotificacionPorId(int id);
    List<Notificacion> obtenerTodasLasNotificaciones();
    List<Notificacion> obtenerNotificacionesNoLeidas();
    void marcarTodasComoLeidas();
}
