package es.um.tds.gastos.persistencia;

import es.um.tds.gastos.modelo.Categoria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria del repositorio de categorías.
 * Utiliza un HashMap para almacenar las categorías.
 * 
 * @author TDS - GestionGastos
 * @version 1.0
 */
public class RepositorioCategoriasMemoria implements IRepositorioCategorias {
    
    private Map<Integer, Categoria> categorias;
    private int siguienteId;
    
    public RepositorioCategoriasMemoria() {
        this.categorias = new HashMap<>();
        this.siguienteId = 1;
        inicializarCategoriasPredefinidas();
    }
    
    @Override
    public Categoria guardar(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        
        // Verificar si ya existe una categoría con el mismo nombre
        if (buscarPorNombre(categoria.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        
        categoria.setId(siguienteId++);
        categorias.put(categoria.getId(), categoria);
        return categoria;
    }
    
    @Override
    public Categoria actualizar(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        if (!categorias.containsKey(categoria.getId())) {
            throw new IllegalArgumentException("La categoría con ID " + categoria.getId() + " no existe");
        }
        
        categorias.put(categoria.getId(), categoria);
        return categoria;
    }
    
    @Override
    public boolean eliminar(int id) {
        return categorias.remove(id) != null;
    }
    
    @Override
    public Optional<Categoria> buscarPorId(int id) {
        return Optional.ofNullable(categorias.get(id));
    }
    
    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categorias.values().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
    
    @Override
    public List<Categoria> obtenerTodas() {
        return new ArrayList<>(categorias.values());
    }
    
    @Override
    public void inicializarCategoriasPredefinidas() {
        for (String nombreCategoria : Categoria.obtenerCategoriasPredefinidas()) {
            if (buscarPorNombre(nombreCategoria).isEmpty()) {
                Categoria categoria = new Categoria(nombreCategoria);
                categoria.setId(siguienteId++);
                categorias.put(categoria.getId(), categoria);
            }
        }
    }
}
