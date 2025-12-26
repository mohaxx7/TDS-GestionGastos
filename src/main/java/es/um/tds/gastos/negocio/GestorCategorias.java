package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.persistencia.IRepositorioCategorias;
import es.um.tds.gastos.persistencia.RepositorioCategoriasMemoria;
import java.util.List;

/**
 * Gestor de negocio para operaciones relacionadas con categorias.
 * Implementa el patron Singleton para garantizar una unica instancia
 * en toda la aplicacion.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GestorCategorias implements IGestorCategorias {

    private static GestorCategorias instancia;
    private IRepositorioCategorias repositorio;

    /**
     * Constructor privado para implementar el patron Singleton.
     */
    private GestorCategorias(IRepositorioCategorias repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
    }

    /**
     * Obtiene la unica instancia del gestor de categorias.
     * 
     * @return la instancia unica de GestorCategorias
     */
    public static GestorCategorias getInstance() {
        if (instancia == null) {
            instancia = new GestorCategorias(new RepositorioCategoriasMemoria());
        }
        return instancia;
    }

    /**
     * Obtiene la instancia del gestor usando un repositorio especifico.
     * 
     * @param repositorio el repositorio a usar
     * @return la instancia unica de GestorCategorias
     */
    public static GestorCategorias getInstance(IRepositorioCategorias repositorio) {
        if (instancia == null) {
            instancia = new GestorCategorias(repositorio);
        }
        return instancia;
    }

    @Override
    public Categoria crearCategoria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        // Verificar si ya existe
        Categoria existente = buscarCategoriaPorNombre(nombre);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
        }

        Categoria categoria = new Categoria(nombre);
        return repositorio.guardar(categoria);
    }

    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        return repositorio.obtenerTodas();
    }

    @Override
    public Categoria buscarCategoriaPorNombre(String nombre) {
        return repositorio.buscarPorNombre(nombre).orElse(null);
    }

    @Override
    public Categoria buscarCategoriaPorId(int id) {
        return repositorio.buscarPorId(id).orElse(null);
    }
}
