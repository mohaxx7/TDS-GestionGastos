package es.um.tds.gastos.persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.um.tds.gastos.modelo.Categoria;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion del repositorio de categorias con persistencia JSON.
 * Utiliza Jackson para serializar/deserializar los datos a un archivo JSON.
 * 
 * Patron aplicado: Repository Pattern
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioCategoriasJSON implements IRepositorioCategorias {

    private static final String ARCHIVO_JSON = "datos/categorias.json";
    private final ObjectMapper mapper;
    private List<Categoria> categorias;
    private int siguienteId;

    /**
     * Constructor que inicializa el repositorio.
     * Carga los datos existentes del archivo JSON si existe.
     */
    public RepositorioCategoriasJSON() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.categorias = cargarDatos();
        this.siguienteId = calcularSiguienteId();

        // Si no hay categorias, inicializar las predefinidas
        if (categorias.isEmpty()) {
            inicializarCategoriasPredefinidas();
        }
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        categoria.setId(siguienteId++);
        categorias.add(categoria);
        guardarDatos();
        return categoria;
    }

    @Override
    public Categoria actualizar(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoria.getId()) {
                categorias.set(i, categoria);
                guardarDatos();
                return categoria;
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(int id) {
        boolean eliminado = categorias.removeIf(c -> c.getId() == id);
        if (eliminado) {
            guardarDatos();
        }
        return eliminado;
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        return categorias.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categorias.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return new ArrayList<>(categorias);
    }

    @Override
    public void inicializarCategoriasPredefinidas() {
        String[] nombresPredefinidos = {
                "Alimentación", "Transporte", "Vivienda", "Ocio",
                "Salud", "Educación", "Ropa", "Otros"
        };

        for (String nombre : nombresPredefinidos) {
            Categoria cat = new Categoria(nombre);
            cat.setId(siguienteId++);
            categorias.add(cat);
        }
        guardarDatos();
    }

    private List<Categoria> cargarDatos() {
        File archivo = new File(ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(archivo, new TypeReference<List<Categoria>>() {
            });
        } catch (IOException e) {
            System.err.println("Error al cargar categorias: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            File archivo = new File(ARCHIVO_JSON);
            archivo.getParentFile().mkdirs();
            mapper.writeValue(archivo, categorias);
        } catch (IOException e) {
            System.err.println("Error al guardar categorias: " + e.getMessage());
        }
    }

    private int calcularSiguienteId() {
        return categorias.stream()
                .mapToInt(Categoria::getId)
                .max()
                .orElse(0) + 1;
    }
}
