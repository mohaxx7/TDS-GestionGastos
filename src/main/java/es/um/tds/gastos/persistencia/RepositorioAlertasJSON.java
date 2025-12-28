package es.um.tds.gastos.persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.um.tds.gastos.modelo.Alerta;
import es.um.tds.gastos.modelo.Notificacion;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacion del repositorio de alertas con persistencia JSON.
 * Guarda alertas y notificaciones en archivos JSON separados.
 * 
 * Patron aplicado: Repository Pattern
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioAlertasJSON implements IRepositorioAlertas {

    private static final String ARCHIVO_ALERTAS = "datos/alertas.json";
    private static final String ARCHIVO_NOTIFICACIONES = "datos/notificaciones.json";
    private final ObjectMapper mapper;
    private List<Alerta> alertas;
    private List<Notificacion> notificaciones;
    private int siguienteIdAlerta;
    private int siguienteIdNotificacion;

    public RepositorioAlertasJSON() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.alertas = cargarAlertas();
        this.notificaciones = cargarNotificaciones();
        this.siguienteIdAlerta = calcularSiguienteIdAlerta();
        this.siguienteIdNotificacion = calcularSiguienteIdNotificacion();
    }

    // OPERACIONES DE ALERTAS

    @Override
    public Alerta guardarAlerta(Alerta alerta) {
        alerta.setId(siguienteIdAlerta++);
        alertas.add(alerta);
        guardarAlertas();
        return alerta;
    }

    @Override
    public void actualizarAlerta(Alerta alerta) {
        for (int i = 0; i < alertas.size(); i++) {
            if (alertas.get(i).getId() == alerta.getId()) {
                alertas.set(i, alerta);
                guardarAlertas();
                return;
            }
        }
    }

    @Override
    public void eliminarAlerta(int id) {
        alertas.removeIf(a -> a.getId() == id);
        guardarAlertas();
    }

    @Override
    public Alerta buscarAlertaPorId(int id) {
        return alertas.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Alerta> obtenerTodasLasAlertas() {
        return new ArrayList<>(alertas);
    }

    @Override
    public List<Alerta> obtenerAlertasActivas() {
        return alertas.stream()
                .filter(Alerta::isActiva)
                .collect(Collectors.toList());
    }

    // OPERACIONES DE NOTIFICACIONES

    @Override
    public Notificacion guardarNotificacion(Notificacion notificacion) {
        notificacion.setId(siguienteIdNotificacion++);
        notificaciones.add(notificacion);
        guardarNotificaciones();
        return notificacion;
    }

    @Override
    public void actualizarNotificacion(Notificacion notificacion) {
        for (int i = 0; i < notificaciones.size(); i++) {
            if (notificaciones.get(i).getId() == notificacion.getId()) {
                notificaciones.set(i, notificacion);
                guardarNotificaciones();
                return;
            }
        }
    }

    @Override
    public void eliminarNotificacion(int id) {
        notificaciones.removeIf(n -> n.getId() == id);
        guardarNotificaciones();
    }

    @Override
    public Notificacion buscarNotificacionPorId(int id) {
        return notificaciones.stream()
                .filter(n -> n.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return new ArrayList<>(notificaciones);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesNoLeidas() {
        return notificaciones.stream()
                .filter(n -> !n.isLeida())
                .collect(Collectors.toList());
    }

    @Override
    public void marcarTodasComoLeidas() {
        notificaciones.forEach(n -> n.marcarComoLeida());
        guardarNotificaciones();
    }

    // METODOS PRIVADOS

    private List<Alerta> cargarAlertas() {
        File archivo = new File(ARCHIVO_ALERTAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(archivo, new TypeReference<List<Alerta>>() {
            });
        } catch (IOException e) {
            System.err.println("Error al cargar alertas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Notificacion> cargarNotificaciones() {
        File archivo = new File(ARCHIVO_NOTIFICACIONES);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(archivo, new TypeReference<List<Notificacion>>() {
            });
        } catch (IOException e) {
            System.err.println("Error al cargar notificaciones: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarAlertas() {
        try {
            File archivo = new File(ARCHIVO_ALERTAS);
            archivo.getParentFile().mkdirs();
            mapper.writeValue(archivo, alertas);
        } catch (IOException e) {
            System.err.println("Error al guardar alertas: " + e.getMessage());
        }
    }

    private void guardarNotificaciones() {
        try {
            File archivo = new File(ARCHIVO_NOTIFICACIONES);
            archivo.getParentFile().mkdirs();
            mapper.writeValue(archivo, notificaciones);
        } catch (IOException e) {
            System.err.println("Error al guardar notificaciones: " + e.getMessage());
        }
    }

    private int calcularSiguienteIdAlerta() {
        return alertas.stream()
                .mapToInt(Alerta::getId)
                .max()
                .orElse(0) + 1;
    }

    private int calcularSiguienteIdNotificacion() {
        return notificaciones.stream()
                .mapToInt(Notificacion::getId)
                .max()
                .orElse(0) + 1;
    }
}
