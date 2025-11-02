package es.um.tds.gastos.modelo;

import java.time.LocalDateTime;

/**
 * Representa una notificación generada por el sistema de alertas.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class Notificacion {
    
    private int id;
    private String mensaje;
    private LocalDateTime fechaHora;
    private Alerta alertaAsociada;
    private boolean leida;
    
    public Notificacion(String mensaje, Alerta alertaAsociada) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        if (alertaAsociada == null) {
            throw new IllegalArgumentException("La alerta asociada no puede ser nula");
        }
        
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
        this.alertaAsociada = alertaAsociada;
        this.leida = false;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        this.mensaje = mensaje;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public Alerta getAlertaAsociada() {
        return alertaAsociada;
    }
    
    public void setAlertaAsociada(Alerta alertaAsociada) {
        if (alertaAsociada == null) {
            throw new IllegalArgumentException("La alerta no puede ser nula");
        }
        this.alertaAsociada = alertaAsociada;
    }
    
    public boolean isLeida() {
        return leida;
    }
    
    public void marcarComoLeida() {
        this.leida = true;
    }
    
    @Override
    public String toString() {
        return String.format("Notificación[%s - %s] %s", 
                leida ? "LEÍDA" : "NO LEÍDA",
                fechaHora.toLocalDate(),
                mensaje);
    }
}
