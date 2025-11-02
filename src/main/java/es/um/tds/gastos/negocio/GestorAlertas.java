package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.modelo.Gasto;
import es.um.tds.gastos.modelo.Notificacion;
import es.um.tds.gastos.modelo.TipoAlerta;
import es.um.tds.gastos.persistencia.IRepositorioAlertas;
import es.um.tds.gastos.util.FiltroGastos;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Implementación del gestor de alertas y notificaciones.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GestorAlertas implements IGestorAlertas {
    
    private IRepositorioAlertas repositorio;
    
    public GestorAlertas(IRepositorioAlertas repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
    }
    
    @Override
    public Alerta crearAlerta(TipoAlerta tipo, double limiteGasto) {
        Alerta alerta = new Alerta(tipo, limiteGasto);
        return repositorio.guardarAlerta(alerta);
    }
    
    @Override
    public Alerta crearAlerta(TipoAlerta tipo, double limiteGasto, Categoria categoria) {
        Alerta alerta = new Alerta(tipo, limiteGasto, categoria);
        return repositorio.guardarAlerta(alerta);
    }
    
    @Override
    public void modificarAlerta(int idAlerta, double nuevoLimite) {
        Alerta alerta = repositorio.buscarAlertaPorId(idAlerta);
        if (alerta == null) {
            throw new IllegalArgumentException("No existe una alerta con ese ID");
        }
        alerta.setLimiteGasto(nuevoLimite);
        repositorio.actualizarAlerta(alerta);
    }
    
    @Override
    public void activarAlerta(int idAlerta) {
        Alerta alerta = repositorio.buscarAlertaPorId(idAlerta);
        if (alerta == null) {
            throw new IllegalArgumentException("No existe una alerta con ese ID");
        }
        alerta.setActiva(true);
        repositorio.actualizarAlerta(alerta);
    }
    
    @Override
    public void desactivarAlerta(int idAlerta) {
        Alerta alerta = repositorio.buscarAlertaPorId(idAlerta);
        if (alerta == null) {
            throw new IllegalArgumentException("No existe una alerta con ese ID");
        }
        alerta.setActiva(false);
        repositorio.actualizarAlerta(alerta);
    }
    
    @Override
    public void eliminarAlerta(int idAlerta) {
        repositorio.eliminarAlerta(idAlerta);
    }
    
    @Override
    public List<Alerta> obtenerTodasLasAlertas() {
        return repositorio.obtenerTodasLasAlertas();
    }
    
    @Override
    public List<Alerta> obtenerAlertasActivas() {
        return repositorio.obtenerAlertasActivas();
    }
    
    @Override
    public List<Notificacion> obtenerNotificacionesNoLeidas() {
        return repositorio.obtenerNotificacionesNoLeidas();
    }
    
    @Override
    public void marcarNotificacionComoLeida(int idNotificacion) {
        Notificacion notificacion = repositorio.buscarNotificacionPorId(idNotificacion);
        if (notificacion == null) {
            throw new IllegalArgumentException("No existe una notificación con ese ID");
        }
        notificacion.marcarComoLeida();
        repositorio.actualizarNotificacion(notificacion);
    }
    
    @Override
    public void marcarTodasLasNotificacionesComoLeidas() {
        repositorio.marcarTodasComoLeidas();
    }
    
    @Override
    public void verificarAlertas(List<Gasto> gastos) {
        List<Alerta> alertasActivas = repositorio.obtenerAlertasActivas();
        
        for (Alerta alerta : alertasActivas) {
            double gastoTotal = calcularGastoSegunAlerta(gastos, alerta);
            
            if (alerta.superaLimite(gastoTotal)) {
                generarNotificacion(alerta, gastoTotal);
            }
        }
    }
    
    private double calcularGastoSegunAlerta(List<Gasto> gastos, Alerta alerta) {
        LocalDate hoy = LocalDate.now();
        List<Gasto> gastosFiltrados;
        
        if (alerta.getTipo() == TipoAlerta.SEMANAL) {
            LocalDate inicioSemana = hoy.minusDays(7);
            gastosFiltrados = FiltroGastos.filtrarPorRangoFechas(gastos, inicioSemana, hoy);
        } else {
            YearMonth mesActual = YearMonth.of(hoy.getYear(), hoy.getMonthValue());
            gastosFiltrados = FiltroGastos.filtrarPorMes(gastos, mesActual);
        }
        
        if (alerta.getCategoria() != null) {
            gastosFiltrados = FiltroGastos.filtrarPorCategoria(gastosFiltrados, alerta.getCategoria());
        }
        
        return FiltroGastos.calcularTotal(gastosFiltrados);
    }
    
    private void generarNotificacion(Alerta alerta, double gastoActual) {
        String mensaje = construirMensajeNotificacion(alerta, gastoActual);
        Notificacion notificacion = new Notificacion(mensaje, alerta);
        repositorio.guardarNotificacion(notificacion);
    }
    
    private String construirMensajeNotificacion(Alerta alerta, double gastoActual) {
        String periodo = alerta.getTipo() == TipoAlerta.SEMANAL ? "semanal" : "mensual";
        String categoria = alerta.getCategoria() != null 
                ? " en " + alerta.getCategoria().getNombre() 
                : "";
        
        return String.format("⚠️ Has superado tu límite %s%s: %.2f€ de %.2f€",
                periodo, categoria, gastoActual, alerta.getLimiteGasto());
    }
}
