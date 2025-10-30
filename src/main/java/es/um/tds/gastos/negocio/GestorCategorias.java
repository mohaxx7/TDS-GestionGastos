package es.um.tds.gastos.negocio;

import es.um.tds.gastos.modelo.Categoria;
import es.um.tds.gastos.persistencia.IRepositorioCategorias;
import java.util.List;

/**
 * Gestor de negocio para operaciones relacionadas con categorías.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class GestorCategorias implements IGestorCategorias {
    
    private IRepositorioCategorias repositorio;
    
    public GestorCategorias(IRepositorioCategorias repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
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
