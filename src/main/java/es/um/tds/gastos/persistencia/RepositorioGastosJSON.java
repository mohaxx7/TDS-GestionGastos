package es.um.tds.gastos.persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.um.tds.gastos.modelo.Gasto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion del repositorio de gastos con persistencia JSON.
 * Utiliza Jackson para serializar/deserializar los datos a un archivo JSON.
 * 
 * Patron aplicado: Repository Pattern
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioGastosJSON implements IRepositorioGastos {

    private static final String ARCHIVO_JSON = "datos/gastos.json";
    private final ObjectMapper mapper;
    private List<Gasto> gastos;
    private int siguienteId;

    /**
     * Constructor que inicializa el repositorio.
     * Carga los datos existentes del archivo JSON si existe.
     */
    public RepositorioGastosJSON() {
        this.mapper = new ObjectMapper();
        // Registrar modulo para manejar LocalDate
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.gastos = cargarDatos();
        this.siguienteId = calcularSiguienteId();
    }

    @Override
    public Gasto guardar(Gasto gasto) {
        gasto.setId(siguienteId++);
        gastos.add(gasto);
        guardarDatos();
        return gasto;
    }

    @Override
    public Gasto actualizar(Gasto gasto) {
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == gasto.getId()) {
                gastos.set(i, gasto);
                guardarDatos();
                return gasto;
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(int id) {
        boolean eliminado = gastos.removeIf(g -> g.getId() == id);
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    @Override
    public Optional<Gasto> buscarPorId(int id) {
        return gastos.stream()
                .filter(g -> g.getId() == id)
                .findFirst();
    }

    @Override
    public List<Gasto> obtenerTodos() {
        return new ArrayList<>(gastos);
    }

    @Override
    public void eliminarTodos() {
        gastos.clear();
        siguienteId = 1;
        guardarDatos();
    }

    /**
     * Carga los datos del archivo JSON.
     * Si el archivo no existe, devuelve una lista vacia.
     */
    private List<Gasto> cargarDatos() {
        File archivo = new File(ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(archivo, new TypeReference<List<Gasto>>() {
            });
        } catch (IOException e) {
            System.err.println("Error al cargar gastos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda los datos en el archivo JSON.
     * Crea el directorio si no existe.
     */
    private void guardarDatos() {
        try {
            File archivo = new File(ARCHIVO_JSON);
            archivo.getParentFile().mkdirs();
            mapper.writeValue(archivo, gastos);
        } catch (IOException e) {
            System.err.println("Error al guardar gastos: " + e.getMessage());
        }
    }

    /**
     * Calcula el siguiente ID disponible basandose en los datos cargados.
     */
    private int calcularSiguienteId() {
        return gastos.stream()
                .mapToInt(Gasto::getId)
                .max()
                .orElse(0) + 1;
    }
}
