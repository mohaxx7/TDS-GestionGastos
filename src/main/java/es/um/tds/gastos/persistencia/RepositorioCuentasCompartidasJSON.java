package es.um.tds.gastos.persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.um.tds.gastos.modelo.CuentaCompartida;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de cuentas compartidas con persistencia JSON.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioCuentasCompartidasJSON {

    private static final String ARCHIVO = "datos/cuentas_compartidas.json";
    private final ObjectMapper mapper;
    private List<CuentaCompartida> cuentas;
    private int siguienteId;

    public RepositorioCuentasCompartidasJSON() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.cuentas = cargarDatos();
        this.siguienteId = calcularSiguienteId();
    }

    public CuentaCompartida guardar(CuentaCompartida cuenta) {
        cuenta.setId(siguienteId++);
        cuentas.add(cuenta);
        guardarDatos();
        return cuenta;
    }

    public void actualizar(CuentaCompartida cuenta) {
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getId() == cuenta.getId()) {
                cuentas.set(i, cuenta);
                guardarDatos();
                return;
            }
        }
    }

    public void eliminar(int id) {
        cuentas.removeIf(c -> c.getId() == id);
        guardarDatos();
    }

    public void eliminar(CuentaCompartida cuenta) {
        eliminar(cuenta.getId());
    }

    public CuentaCompartida buscarPorId(int id) {
        return cuentas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<CuentaCompartida> obtenerTodas() {
        return new ArrayList<>(cuentas);
    }

    private List<CuentaCompartida> cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(archivo, new TypeReference<List<CuentaCompartida>>() {
            });
        } catch (IOException e) {
            System.err.println("Error al cargar cuentas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            File archivo = new File(ARCHIVO);
            archivo.getParentFile().mkdirs();
            mapper.writeValue(archivo, cuentas);
        } catch (IOException e) {
            System.err.println("Error al guardar cuentas: " + e.getMessage());
        }
    }

    private int calcularSiguienteId() {
        return cuentas.stream()
                .mapToInt(CuentaCompartida::getId)
                .max()
                .orElse(0) + 1;
    }
}
