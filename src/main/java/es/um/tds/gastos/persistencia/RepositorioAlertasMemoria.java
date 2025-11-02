package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Notificacion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de alertas y notificaciones.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioAlertasMemoria implements IRepositorioAlertas {
    
    private Map<Integer, Alerta> alertas;
    private Map<Integer, Notificacion> notificaciones;
    private int siguienteIdAlerta;
    private int siguienteIdNotificacion;
    
    public RepositorioAlertasMemoria() {
        this.alertas = new HashMap<>();
        this.notificaciones = new HashMap<>();
        this.siguienteIdAlerta = 1;
        this.siguienteIdNotificacion = 1;
    }
    
    @Override
    public Alerta guardarAlerta(Alerta alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("La alerta no puede ser nula");
        }
        alerta.setId(siguienteIdAlerta++);
        alertas.put(alerta.getId(), alerta);
        return alerta;
    }
    
    @Override
    public void actualizarAlerta(Alerta alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("La alerta no puede ser nula");
        }
        if (!alertas.containsKey(alerta.getId())) {
            throw new IllegalArgumentException("La alerta no existe en el repositorio");
        }
        alertas.put(alerta.getId(), alerta);
    }
    
    @Override
    public void eliminarAlerta(int id) {
        if (!alertas.containsKey(id)) {
            throw new IllegalArgumentException("No existe una alerta con ese ID");
        }
        alertas.remove(id);
    }
    
    @Override
    public Alerta buscarAlertaPorId(int id) {
        return alertas.get(id);
    }
    
    @Override
    public List<Alerta> obtenerTodasLasAlertas() {
        return alertas.values().stream().collect(Collectors.toList());
    }
    
    @Override
    public List<Alerta> obtenerAlertasActivas() {
        return alertas.values().stream()
                .filter(Alerta::isActiva)
                .collect(Collectors.toList());
    }
    
    @Override
    public Notificacion guardarNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificación no puede ser nula");
        }
        notificacion.setId(siguienteIdNotificacion++);
        notificaciones.put(notificacion.getId(), notificacion);
        return notificacion;
    }
    
    @Override
    public void actualizarNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificación no puede ser nula");
        }
        if (!notificaciones.containsKey(notificacion.getId())) {
            throw new IllegalArgumentException("La notificación no existe en el repositorio");
        }
        notificaciones.put(notificacion.getId(), notificacion);
    }
    
    @Override
    public void eliminarNotificacion(int id) {
        if (!notificaciones.containsKey(id)) {
            throw new IllegalArgumentException("No existe una notificación con ese ID");
        }
        notificaciones.remove(id);
    }
    
    @Override
    public Notificacion buscarNotificacionPorId(int id) {
        return notificaciones.get(id);
    }
    
    @Override
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return notificaciones.values().stream().collect(Collectors.toList());
    }
    
    @Override
    public List<Notificacion> obtenerNotificacionesNoLeidas() {
        return notificaciones.values().stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
    }
    
    @Override
    public void marcarTodasComoLeidas() {
        notificaciones.values().forEach(Notificacion::marcarComoLeida);
    }
}
